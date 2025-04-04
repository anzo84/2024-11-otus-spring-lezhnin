import $ from 'jquery';
import {authorsApi} from './include/apiClient';
import {loadTable, showAlert} from "./include/common";

import modifyAuthor from "otus-book-library/src/model/ModifyAuthor";
import author from "otus-book-library/src/model/Author";

async function reloadAuthorsWithParams(tableBodyId, editDialogId) {
    try {
        const authors = await authorsApi.getAllAuthors();
        loadTable(tableBodyId, editDialogId, authors, [
            author => author.fullName
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

function reloadAuthors() {
    reloadAuthorsWithParams("authorList", "saveDialog").then();
}

$(document).ready(function () {
    reloadAuthors();
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteAuthorAction") {
        authorsApi.deleteAuthor(Number.parseInt($(this).data("param")))
            .then(() => {
                reloadAuthors();
            });
    } else if (action === "editAuthorAction") {
        const id = Number.parseInt($(this).data("param"));
        authorsApi.getAuthorById(id).then(author => {
            $('#authorId').val(id);
            $('#authorFullName').val(author.fullName);
        });
    } else if (action === "saveAuthorAction") {
        let id = Number.parseInt($('#authorId').val());
        let name = $('#authorFullName').val();
        const request = id === 0
            ? authorsApi.createAuthor(new author(name))
            : authorsApi.updateAuthor(id, new modifyAuthor(name));
        request
            .then(() => {
                reloadAuthors();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
