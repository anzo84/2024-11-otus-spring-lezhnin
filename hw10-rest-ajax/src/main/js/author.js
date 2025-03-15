import {authorsApi} from './include/apiClient';
import {reloadTable, showAlert} from "./include/common";
import modifyAuthor from "otus-book-library/src/model/ModifyAuthor";
import author from "otus-book-library/src/model/Author";

import $ from 'jquery';
window.$ = $;

async function reloadAuthorList(tableBodyId, editDialogId) {
    try {
        const authors = await authorsApi.getAllAuthors();
        reloadTable(tableBodyId, editDialogId, authors, [
            author => author.fullName
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

function reload() {
    reloadAuthorList("authorList", "saveDialog").then();
}

$(document).ready(function () {
    reload();
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteAuthorAction") {
        authorsApi.deleteAuthor(Number.parseInt($(this).data("param")))
            .then(() => {
                reload();
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
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
