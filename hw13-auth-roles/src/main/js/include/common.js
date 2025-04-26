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
        error.body.map(x => x.message).join(";<br>\n") : alert.data("def-message");
    alert.show().html(msg);
}

export function loadTable(tableBodyId, editDialogId, data, columns) {
    const tableBody = $("#" + tableBodyId);
    if (Array.isArray(data) && data.length === 0) {
        $('#emptyInfo').show();
        tableBody.parent().hide();
    } else {
        $('#emptyInfo').hide();
        tableBody.parent().show();
        tableBody.empty();

        let i = 1;
        data.forEach(item => {
            const newRow = $("<tr>");
            const buttonEdit = createEditButton(editDialogId, tableBody, item.id);
            const buttonDelete = createDeleteButton(tableBody, item.id);

            const cells = columns.map(column => $("<td>").text(column(item)));

            const buttonCell = $("<td>")
                .addClass("text-nowrap p-1")
                .append(buttonEdit.data("action").length > 0 ? buttonEdit : null)
                .append(buttonDelete.data("action").length > 0 ? buttonDelete : null)

            if ((buttonEdit.data("action").length + buttonDelete.data("action").length) > 0) {
                cells.push(buttonCell);
            }
            newRow.append($("<td>").text(i++)).append(...cells);
            tableBody.append(newRow);
        });
    }
}

export function loadOptions(element, promise, valueSelector, textSelector) {
    promise.then(items => {
        element.empty();
        if (typeof element.data('not-selected') !== 'undefined') {
            element.append($("<option>")
                .attr("selected", true)
                .val(null)
                .text(element.data('not-selected')));
        }
        items.forEach(item => {
            element.append($("<option>")
                .val(valueSelector(item))
                .text(textSelector(item)));
        });
        element.trigger("change");
    });
}