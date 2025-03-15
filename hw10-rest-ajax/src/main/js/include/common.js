import $ from 'jquery';

export function createEditButton(editDialogId, tableBody, dataParam) {
    return $("<button>")
        .attr("role", "button")
        .attr("data-toggle", "modal")
        .attr("data-target", "#" + editDialogId)
        .attr("data-action", tableBody.data("edit-action"))
        .attr("data-title", tableBody.data("edit-title"))
        .attr("data-param", dataParam)
        .attr("title", tableBody.data("edit-title"))
        .addClass("btn btn-primary")
        .append($("<span>")
            .addClass("fas")
            .addClass("fa-pen-to-square"));
}

export function createDeleteButton(tableBody, dataParam) {
    return $("<button>")
        .addClass("btn btn-danger ml-1")
        .attr("role", "button")
        .attr("title", tableBody.data("delete-title"))
        .attr("data-action", tableBody.data("delete-action"))
        .attr("data-param", dataParam)
        .append($("<span>")
            .addClass("fas")
            .addClass("fa-trash-can"));
}

export function showAlert(error) {
    const alert = $('#alert');
    const msg = error.status === 400 ?
        error.body.map(x => x.message).join(",") : alert.data("def-message");
    alert.show().text(msg);
}