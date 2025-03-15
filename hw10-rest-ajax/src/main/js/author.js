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

import modifyAuthor from "otus-book-library/src/model/ModifyAuthor";
import author from "otus-book-library/src/model/Author";
import {reloadTable, showAlert} from "./include/common";

const OtusBookLibraryApiClient = require('otus-book-library');
const api = new OtusBookLibraryApiClient.AuthorsApi();

async function reloadAuthorList(tableBodyId, editDialogId) {
    try {
        const authors = await api.getAllAuthors();
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
        api.deleteAuthor(Number.parseInt($(this).data("param")))
            .then(() => {
                reload();
            });
    } else if (action === "editAuthorAction") {
        const id = Number.parseInt($(this).data("param"));
        api.getAuthorById(id).then(author => {
            $('#authorId').val(id);
            $('#authorFullName').val(author.fullName);
        });
    } else if (action === "saveAuthorAction") {
        let id = Number.parseInt($('#authorId').val());
        let name = $('#authorFullName').val();
        const request = id === 0
            ? api.createAuthor(new author(name))
            : api.updateAuthor(id, new modifyAuthor(name));
        request
            .then(() => {
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
