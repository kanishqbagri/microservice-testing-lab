package com.kb.testing;

import org.junit.jupiter.api.extension.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class DbTestReportingExtension implements TestWatcher, BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {

	private static final String JDBC_URL = System.getenv("TEST_RESULTS_JDBC_URL");
	private static final String JDBC_USER = Optional.ofNullable(System.getenv("TEST_RESULTS_DB_USER")).orElse("");
	private static final String JDBC_PASSWORD = Optional.ofNullable(System.getenv("TEST_RESULTS_DB_PASSWORD")).orElse("");
	private static final String SERVICE_NAME = Optional.ofNullable(System.getenv("TEST_RESULTS_SERVICE_NAME")).orElse("user-service");
	private static final String TEST_TYPE = Optional.ofNullable(System.getenv("TEST_RESULTS_TEST_TYPE")).orElse("API_TEST");

	private static final boolean REPORTING_ENABLED = JDBC_URL != null && !JDBC_URL.isBlank();

	@Override
	public void beforeAll(ExtensionContext context) {
		if (!REPORTING_ENABLED) return;
		try (Connection connection = getConnection()) {
			ensureTableExists(connection);
		} catch (Exception ignored) {
			// Swallow to not break tests
		}
	}

	@Override
	public void afterAll(ExtensionContext context) {
		// no-op
	}

	@Override
	public void beforeTestExecution(ExtensionContext context) {
		ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
		store.put("start", Instant.now());
	}

	@Override
	public void afterTestExecution(ExtensionContext context) {
		// no-op, handled in TestWatcher callbacks for status
	}

	@Override
	public void testSuccessful(ExtensionContext context) {
		record(context, "PASSED", null);
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		record(context, "FAILED", cause);
	}

	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		record(context, "SKIPPED", cause);
	}

	private void record(ExtensionContext context, String status, Throwable error) {
		if (!REPORTING_ENABLED) return;
		String testName = context.getRequiredTestClass().getSimpleName() + "." + context.getDisplayName();
		ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
		Instant start = store.get("start", Instant.class);
		Instant end = Instant.now();
		long durationMs = start != null ? Duration.between(start, end).toMillis() : 0L;

		try (Connection connection = getConnection()) {
			insertResult(connection, testName, SERVICE_NAME, TEST_TYPE, status, durationMs, Timestamp.from(start != null ? start : end), Timestamp.from(end), error);
		} catch (Exception ignored) {
			// Do not fail tests due to reporting issues
		}
	}

	private static Connection getConnection() throws SQLException {
		if (JDBC_USER.isEmpty() && JDBC_PASSWORD.isEmpty()) {
			return DriverManager.getConnection(JDBC_URL);
		}
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	private static void ensureTableExists(Connection connection) throws SQLException {
		final String sql = "CREATE TABLE IF NOT EXISTS test_results (" +
			"id UUID PRIMARY KEY," +
			"test_name VARCHAR(255) NOT NULL," +
			"service_name VARCHAR(255) NOT NULL," +
			"test_type VARCHAR(50) NOT NULL," +
			"status VARCHAR(50) NOT NULL," +
			"execution_time_ms BIGINT," +
			"start_time TIMESTAMP NOT NULL," +
			"end_time TIMESTAMP," +
			"error_message TEXT," +
			"stack_trace TEXT," +
			"created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
			"updated_at TIMESTAMP" +
		")";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.execute();
		}
	}

	private static void insertResult(
			Connection connection,
			String testName,
			String serviceName,
			String testType,
			String status,
			long executionTimeMs,
			Timestamp startTime,
			Timestamp endTime,
			Throwable error
	) throws SQLException {
		final String insert = "INSERT INTO test_results (" +
			"id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, error_message, stack_trace, updated_at" +
			") VALUES (?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
		try (PreparedStatement ps = connection.prepareStatement(insert)) {
			ps.setObject(1, java.util.UUID.randomUUID());
			ps.setString(2, testName);
			ps.setString(3, serviceName);
			ps.setString(4, testType);
			ps.setString(5, status);
			ps.setLong(6, executionTimeMs);
			ps.setTimestamp(7, startTime);
			ps.setTimestamp(8, endTime);
			ps.setString(9, error != null ? safeTruncate(error.getMessage(), 2000) : null);
			ps.setString(10, error != null ? safeTruncate(getStackTraceAsString(error), 8000) : null);
			ps.executeUpdate();
		}
	}

	private static String safeTruncate(String s, int max) {
		if (s == null) return null;
		return s.length() <= max ? s : s.substring(0, max);
	}

	private static String getStackTraceAsString(Throwable t) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement e : t.getStackTrace()) {
			sb.append(e.toString()).append('\n');
		}
		return sb.toString();
	}
}

