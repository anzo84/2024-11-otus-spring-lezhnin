import {metricsApi} from './include/apiClient';
import $ from 'jquery';

$(document).ready(function () {
    metricsApi.getMetrics().then(metrics => {
        metrics.forEach(metric => {
            $(`#${metric.name}`).text(metric.value);
        });
    })
});

