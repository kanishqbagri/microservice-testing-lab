// run-details.js
// Fill in your Supabase credentials
const supabaseUrl = 'https://YOUR_SUPABASE_URL.supabase.co';
const supabaseKey = 'YOUR_SUPABASE_ANON_KEY';
const supabase = window.supabase.createClient(supabaseUrl, supabaseKey);

// Get run_id from query string
function getRunId() {
    const params = new URLSearchParams(window.location.search);
    return params.get('run_id');
}

async function loadRunDetails() {
    const runId = getRunId();
    if (!runId) return;
    // Fetch run info
    let { data: run, error: runError } = await supabase
        .from('test_run')
        .select('id,started_at,finished_at,status,ci_job_id,ci_job_url,commit_sha,triggered_by,test_suite(name,project(name))')
        .eq('id', runId)
        .single();
    if (runError) return;
    // Render run info
    document.getElementById('run-info').innerHTML = `
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">Run ID: ${run.id}</h5>
                <p class="mb-1"><b>Project:</b> ${run.test_suite?.project?.name || '-'}</p>
                <p class="mb-1"><b>Suite:</b> ${run.test_suite?.name || '-'}</p>
                <p class="mb-1"><b>Status:</b> <span class="status-badge status-${run.status?.toLowerCase()}">${run.status}</span></p>
                <p class="mb-1"><b>Started:</b> ${run.started_at ? new Date(run.started_at).toLocaleString() : '-'}</p>
                <p class="mb-1"><b>Finished:</b> ${run.finished_at ? new Date(run.finished_at).toLocaleString() : '-'}</p>
                <p class="mb-1"><b>Commit SHA:</b> ${run.commit_sha || '-'}</p>
                <p class="mb-1"><b>Triggered By:</b> ${run.triggered_by || '-'}</p>
                <p class="mb-1"><b>CI Job:</b> ${run.ci_job_url ? `<a href='${run.ci_job_url}' target='_blank'>${run.ci_job_id}</a>` : '-'}</p>
            </div>
        </div>
    `;
    // Fetch test results for this run
    let { data: results, error } = await supabase
        .from('test_result')
        .select('id,status,duration_ms,error_message,test_case(name)')
        .eq('run_id', runId);
    if (error) return;
    const tbody = document.getElementById('test-results-table');
    if (!results || results.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">No test results found</td></tr>';
        return;
    }
    tbody.innerHTML = results.map(r => `
        <tr>
            <td>${r.test_case?.name || '-'}</td>
            <td><span class="status-badge status-${r.status.toLowerCase()}">${r.status}</span></td>
            <td>${r.duration_ms || '-'}</td>
            <td>${r.error_message ? r.error_message.substring(0, 100) : '-'}</td>
        </tr>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadRunDetails);
