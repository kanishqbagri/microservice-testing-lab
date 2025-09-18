import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 10 },   // Ramp-up to 10 users
        { duration: '2m', target: 50 },   // Ramp-up to 50 users
        { duration: '2m', target: 100 },  // Ramp-up to 100 users
        { duration: '2m', target: 200 },  // Ramp-up to 200 users
        { duration: '2m', target: 0 },    // Ramp-down
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests < 500ms
        http_req_failed: ['rate<0.01'],  // <1% errors
    },
};

const BASE_URL = 'http://localhost:8082'; // Change to your order-service URL

export default function () {
    // Example: Place an order (POST)
    let payload = JSON.stringify({
        userId: 1,
        productId: 2,
        quantity: Math.floor(Math.random() * 10) + 1,
    });
    let params = {
        headers: { 'Content-Type': 'application/json' },
    };
    let res = http.post(`${BASE_URL}/api/orders`, payload, params);
    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 1s': (r) => r.timings.duration < 1000,
    });
    sleep(1);
}
