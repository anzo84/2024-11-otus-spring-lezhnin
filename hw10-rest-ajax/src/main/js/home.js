import 'popper.js';
import $ from 'jquery';

window.$ = $;
window.jQuery = $;

import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'admin-lte/dist/css/adminlte.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './style.css';
import 'admin-lte/dist/js/adminlte.min.js';
import '@fortawesome/fontawesome-free/js/all.min.js';

const OtusBookLibraryApiClient = require('otus-book-library');
const api = new OtusBookLibraryApiClient.MetricsApi();

async function reloadMetrics() {
    try {
        const metrics = await api.getMetrics();
        metrics.forEach(metric => {
            $(`#${metric.name}`).text(metric.value);
        });
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

$(document).ready(function () {
    reloadMetrics().then();
});

