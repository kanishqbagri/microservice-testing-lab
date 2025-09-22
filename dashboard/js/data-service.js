// Data Service - Handles all Supabase data fetching and pagination
import { CONFIG } from './config.js';

export class DataService {
    constructor(supabase) {
        this.supabase = supabase;
    }

    // Debug method to query table counts
    async debugTableCounts(threeMonthsAgoISO) {
        console.log('=== DEBUG: Querying table counts ===');
        
        try {
            // Count total test_run records in last 3 months
            const { count: testRunCount, error: testRunError } = await this.supabase
                .from('test_run')
                .select('*', { count: 'exact', head: true })
                .gte('started_at', threeMonthsAgoISO);
            
            if (testRunError) {
                console.error('Error counting test_run records:', testRunError);
            } else {
                console.log(`📊 Total test_run records in last 3 months: ${testRunCount}`);
            }

            // Count total test_result records in last 3 months
            const { count: testResultCount, error: testResultError } = await this.supabase
                .from('test_result')
                .select('*', { count: 'exact', head: true })
                .gte('created_at', threeMonthsAgoISO);
            
            if (testResultError) {
                console.error('Error counting test_result records:', testResultError);
            } else {
                console.log(`📊 Total test_result records in last 3 months: ${testResultCount}`);
            }

            // Count total records in each table (no date filter)
            const { count: totalTestRunCount, error: totalTestRunError } = await this.supabase
                .from('test_run')
                .select('*', { count: 'exact', head: true });
            
            if (totalTestRunError) {
                console.error('Error counting total test_run records:', totalTestRunError);
            } else {
                console.log(`📊 Total test_run records in database: ${totalTestRunCount}`);
            }

            const { count: totalTestResultCount, error: totalTestResultError } = await this.supabase
                .from('test_result')
                .select('*', { count: 'exact', head: true });
            
            if (totalTestResultError) {
                console.error('Error counting total test_result records:', totalTestResultError);
            } else {
                console.log(`📊 Total test_result records in database: ${totalTestResultCount}`);
            }

            console.log('=== END DEBUG: Table counts ===');
        } catch (error) {
            console.error('Error in debug table counts:', error);
        }
    }

    // Pagination function to fetch all records
    async fetchAllRecords(table, options = {}) {
        const { select = '*', filter = null, orderBy = null, pageSize = CONFIG.DATA.PAGE_SIZE } = options;
        let allRecords = [];
        let from = 0;
        let hasMore = true;
        let pageCount = 0;

        console.log(`Starting paginated fetch for ${table}...`);

        while (hasMore) {
            pageCount++;
            let query = this.supabase
                .from(table)
                .select(select)
                .range(from, from + pageSize - 1);

            // Apply filter if provided
            if (filter) {
                query = query[filter.operator](filter.column, filter.value);
            }

            // Apply ordering if provided
            if (orderBy) {
                query = query.order(orderBy.column, { ascending: orderBy.ascending });
            }

            const { data, error } = await query;

            if (error) {
                console.error(`Error fetching ${table} records:`, error);
                throw error;
            }

            if (data && data.length > 0) {
                allRecords = allRecords.concat(data);
                from += pageSize;
                hasMore = data.length === pageSize; // Continue if we got a full page
                console.log(`Fetched ${data.length} ${table} records (total: ${allRecords.length})`);
            } else {
                hasMore = false;
            }
        }

        console.log(`Completed paginated fetch for ${table}: ${allRecords.length} total records`);
        return allRecords;
    }

    // Fetch test runs for last 3 months
    async fetchTestRuns(threeMonthsAgoISO) {
        return await this.fetchAllRecords('test_run', {
            select: `id, status, started_at, finished_at, test_suite(name, project(name))`,
            filter: { column: 'started_at', operator: 'gte', value: threeMonthsAgoISO },
            orderBy: { column: 'started_at', ascending: false }
        });
    }

    // Fetch test results for last 3 months
    async fetchTestResults(threeMonthsAgoISO) {
        return await this.fetchAllRecords('test_result', {
            select: `id, status, duration_ms, created_at, test_case(name, tags), test_run(test_suite(name, project(name)))`,
            filter: { column: 'created_at', operator: 'gte', value: threeMonthsAgoISO },
            orderBy: { column: 'created_at', ascending: false }
        });
    }

    // Get date range for last N months
    getDateRange(months = CONFIG.DATA.TIME_RANGE_MONTHS) {
        const date = new Date();
        date.setMonth(date.getMonth() - months);
        return {
            start: date.toISOString(),
            end: new Date().toISOString(),
            display: `${date.toLocaleDateString()} - ${new Date().toLocaleDateString()}`
        };
    }
}
