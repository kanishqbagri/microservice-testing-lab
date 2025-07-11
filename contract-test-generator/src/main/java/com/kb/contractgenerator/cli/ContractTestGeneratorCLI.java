package com.kb.contractgenerator.cli;

import com.kb.contractgenerator.model.ContractTestRequest;
import com.kb.contractgenerator.model.ContractTestResponse;
import com.kb.contractgenerator.service.ContractTestGeneratorService;
import com.kb.contractgenerator.service.SwaggerParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Command(
    name = "contract-test-generator",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Generate contract tests from Swagger/OpenAPI specifications"
)
public class ContractTestGeneratorCLI implements CommandLineRunner {

    @Autowired
    private ContractTestGeneratorService generatorService;

    @Autowired
    private SwaggerParserService swaggerParserService;

    @Option(names = {"-p", "--provider"}, description = "Provider service name", required = true)
    private String providerName;

    @Option(names = {"-c", "--consumer"}, description = "Consumer service name", required = true)
    private String consumerName;

    @Option(names = {"-u", "--url"}, description = "Swagger/OpenAPI specification URL")
    private String swaggerUrl;

    @Option(names = {"-f", "--file"}, description = "Swagger/OpenAPI specification file path")
    private String swaggerFile;

    @Option(names = {"-o", "--output"}, description = "Output directory for generated tests", defaultValue = "generated-contracts")
    private String outputDirectory;

    @Option(names = {"-t", "--framework"}, description = "Test framework (pact, spring-cloud-contract)", defaultValue = "pact")
    private String testFramework;

    @Option(names = {"-l", "--language"}, description = "Programming language (java, kotlin, groovy)", defaultValue = "java")
    private String language;

    @Option(names = {"--port"}, description = "Service port for tests", defaultValue = "8080")
    private Integer port;

    @Option(names = {"--base-url"}, description = "Base URL for the service")
    private String baseUrl;

    @Option(names = {"--include-paths"}, description = "Comma-separated list of paths to include (regex patterns)")
    private String includePaths;

    @Option(names = {"--exclude-paths"}, description = "Comma-separated list of paths to exclude (regex patterns)")
    private String excludePaths;

    @Option(names = {"--headers"}, description = "Custom headers in format 'key1:value1,key2:value2'")
    private String customHeaders;

    @Option(names = {"--verbose"}, description = "Enable verbose output")
    private boolean verbose;

    @Override
    public void run(String... args) throws Exception {
        // Only run if CLI mode is enabled
        if (args.length > 0 && args[0].equals("cli")) {
            // Remove the "cli" argument
            String[] cliArgs = Arrays.copyOfRange(args, 1, args.length);
            
            int exitCode = new CommandLine(this).execute(cliArgs);
            System.exit(exitCode);
        }
    }

    @CommandLine.Command(name = "generate", description = "Generate contract tests")
    public Integer generate() {
        try {
            System.out.println("🔧 Contract Test Generator");
            System.out.println("==========================");
            
            // Validate inputs
            if (swaggerUrl == null && swaggerFile == null) {
                System.err.println("❌ Error: Either --url or --file must be specified");
                return 1;
            }

            // Create request
            ContractTestRequest request = createRequest();
            
            if (verbose) {
                printRequestDetails(request);
            }

            // Generate tests
            System.out.println("🚀 Generating contract tests...");
            ContractTestResponse response = generatorService.generateContractTests(request);

            if (response.isSuccess()) {
                System.out.println("✅ " + response.getMessage());
                printGeneratedTests(response);
                return 0;
            } else {
                System.err.println("❌ " + response.getMessage());
                if (response.getErrors() != null) {
                    response.getErrors().forEach(error -> System.err.println("  - " + error));
                }
                return 1;
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
            return 1;
        }
    }

    private ContractTestRequest createRequest() throws IOException {
        ContractTestRequest request = new ContractTestRequest();
        request.setProviderName(providerName);
        request.setConsumerName(consumerName);
        request.setTestFramework(testFramework);
        request.setLanguage(language);
        request.setOutputDirectory(outputDirectory);
        request.setPort(port);
        request.setBaseUrl(baseUrl);

        // Parse Swagger specification
        if (swaggerUrl != null) {
            request.setSwaggerSpec(swaggerParserService.parseSwaggerFromUrl(swaggerUrl));
        } else if (swaggerFile != null) {
            String content = Files.readString(Paths.get(swaggerFile));
            request.setSwaggerSpec(swaggerParserService.parseSwaggerSpec(content));
        }

        // Parse include/exclude paths
        if (includePaths != null) {
            request.setIncludePaths(Arrays.asList(includePaths.split(",")));
        }
        if (excludePaths != null) {
            request.setExcludePaths(Arrays.asList(excludePaths.split(",")));
        }

        // Parse custom headers
        if (customHeaders != null) {
            Map<String, String> headers = new java.util.HashMap<>();
            for (String header : customHeaders.split(",")) {
                String[] parts = header.split(":");
                if (parts.length == 2) {
                    headers.put(parts[0].trim(), parts[1].trim());
                }
            }
            request.setCustomHeaders(headers);
        }

        return request;
    }

    private void printRequestDetails(ContractTestRequest request) {
        System.out.println("\n📋 Request Details:");
        System.out.println("  Provider: " + request.getProviderName());
        System.out.println("  Consumer: " + request.getConsumerName());
        System.out.println("  Framework: " + request.getTestFramework());
        System.out.println("  Language: " + request.getLanguage());
        System.out.println("  Output Directory: " + request.getOutputDirectory());
        System.out.println("  Port: " + request.getPort());
        if (request.getBaseUrl() != null) {
            System.out.println("  Base URL: " + request.getBaseUrl());
        }
        if (request.getIncludePaths() != null) {
            System.out.println("  Include Paths: " + String.join(", ", request.getIncludePaths()));
        }
        if (request.getExcludePaths() != null) {
            System.out.println("  Exclude Paths: " + String.join(", ", request.getExcludePaths()));
        }
        if (request.getCustomHeaders() != null) {
            System.out.println("  Custom Headers: " + request.getCustomHeaders());
        }
        System.out.println();
    }

    private void printGeneratedTests(ContractTestResponse response) {
        if (response.getGeneratedTests() != null && !response.getGeneratedTests().isEmpty()) {
            System.out.println("\n📁 Generated Tests:");
            for (ContractTestResponse.GeneratedTest test : response.getGeneratedTests()) {
                System.out.println("  ✅ " + test.getTestName() + " (" + test.getTestType() + ")");
                System.out.println("     📄 " + test.getFilePath());
                if (test.getMetadata() != null) {
                    test.getMetadata().forEach((key, value) -> 
                        System.out.println("     📊 " + key + ": " + value));
                }
            }
        }

        if (response.getWarnings() != null && !response.getWarnings().isEmpty()) {
            System.out.println("\n⚠️  Warnings:");
            response.getWarnings().forEach(warning -> System.out.println("  - " + warning));
        }
    }

    @CommandLine.Command(name = "list-frameworks", description = "List supported test frameworks")
    public Integer listFrameworks() {
        System.out.println("🔧 Supported Test Frameworks:");
        System.out.println("  pact - Pact Contract Testing Framework");
        System.out.println("  spring-cloud-contract - Spring Cloud Contract");
        return 0;
    }

    @CommandLine.Command(name = "list-languages", description = "List supported programming languages")
    public Integer listLanguages() {
        System.out.println("🔧 Supported Programming Languages:");
        System.out.println("  java - Java");
        System.out.println("  kotlin - Kotlin");
        System.out.println("  groovy - Groovy");
        return 0;
    }

    @CommandLine.Command(name = "validate", description = "Validate Swagger/OpenAPI specification")
    public Integer validate() {
        try {
            System.out.println("🔍 Validating Swagger/OpenAPI specification...");
            
            if (swaggerUrl != null) {
                var spec = swaggerParserService.parseSwaggerFromUrl(swaggerUrl);
                System.out.println("✅ Valid Swagger specification from URL");
                printSpecificationDetails(spec);
            } else if (swaggerFile != null) {
                String content = Files.readString(Paths.get(swaggerFile));
                var spec = swaggerParserService.parseSwaggerSpec(content);
                System.out.println("✅ Valid Swagger specification from file");
                printSpecificationDetails(spec);
            } else {
                System.err.println("❌ Error: Either --url or --file must be specified");
                return 1;
            }
            
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Invalid Swagger specification: " + e.getMessage());
            return 1;
        }
    }

    private void printSpecificationDetails(com.kb.contractgenerator.model.SwaggerSpec spec) {
        System.out.println("\n📋 Specification Details:");
        if (spec.getInfo() != null) {
            System.out.println("  Title: " + spec.getInfo().getTitle());
            System.out.println("  Version: " + spec.getInfo().getVersion());
            if (spec.getInfo().getDescription() != null) {
                System.out.println("  Description: " + spec.getInfo().getDescription());
            }
        }
        System.out.println("  OpenAPI Version: " + spec.getOpenapi());
        System.out.println("  Endpoints: " + spec.getPaths().size());
        
        if (verbose) {
            System.out.println("\n📋 Endpoints:");
            spec.getPaths().forEach((path, pathItem) -> {
                if (pathItem.getGet() != null) System.out.println("  GET  " + path);
                if (pathItem.getPost() != null) System.out.println("  POST " + path);
                if (pathItem.getPut() != null) System.out.println("  PUT  " + path);
                if (pathItem.getDelete() != null) System.out.println("  DELETE " + path);
                if (pathItem.getPatch() != null) System.out.println("  PATCH " + path);
            });
        }
    }
} 