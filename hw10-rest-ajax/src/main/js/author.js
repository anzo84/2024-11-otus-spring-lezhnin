import 'popper.js';
import $ from 'jquery';

window.$ = $;
window.jQuery = $;

// Импорт Bootstrap
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

// Импорт AdminLTE
import 'admin-lte/dist/css/adminlte.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'select2/dist/css/select2.min.css'
import './style.css';

import 'admin-lte/dist/js/adminlte.min.js';
import '@fortawesome/fontawesome-free/js/all.min.js';
import 'select2/dist/js/select2.full.min'

import updateAuthorRequest from "otus-book-library/src/model/UpdateAuthorRequest";
import author from "otus-book-library/src/model/Author";

// Инициализация AdminLTE
$(function () {
    $('[data-widget="pushmenu"]').PushMenu('toggle')
});

const OtusBookLibraryApiClient = require('api__');
const api = new OtusBookLibraryApiClient.AuthorsApi();

async function reloadAuthorList(tableBodyId, editDialogId) {
    try {
        let tableBody = $("#" + tableBodyId);
        const authors = await api.getAllAuthors();
        if (Array.isArray(authors) && authors.length === 0) {
            $('#emptyInfo').show();
            tableBody.parent().hide();
        } else {
            $('#emptyInfo').hide();
            tableBody.parent().show();
            tableBody.empty();

            let i = 1;
            authors.forEach(author => {
                const newRow = $("<tr>");
                const buttonEdit = $("<button>")
                    .attr("id", "edit" + tableBodyId + i)
                    .attr("role", "button")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#" + editDialogId)
                    .attr("data-action", tableBody.data("edit-action"))
                    .attr("data-title", tableBody.data("edit-title"))
                    .attr("data-param", author.id)
                    .attr("title", tableBody.data("edit-title"))
                    .addClass("btn btn-primary")
                    .append($("<span>")
                        .addClass("fas")
                        .addClass("fa-pen-to-square"));

                const buttonDelete = $("<button>")
                    .addClass("btn btn-danger ml-1")
                    .attr("role", "button")
                    .attr("title", tableBody.data("delete-title"))
                    .attr("data-action", tableBody.data("delete-action"))
                    .attr("data-param", author.id)
                    .append($("<span>")
                        .addClass("fas")
                        .addClass("fa-trash-can"));

                newRow.append(
                    $("<td>").text(i++),
                    $("<td>").text(author.fullName),
                    $("<td>")
                        .addClass("text-nowrap p-1")
                        .append(buttonEdit)
                        .append(buttonDelete)
                );
                tableBody.append(newRow);
            });
        }
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
    } else if (action === "newAuthorAction") {
        $('#authorId').val(0);
        $('#authorFullName').val("");
    } else if (action === "saveAuthorAction") {
        let id = Number.parseInt($('#authorId').val());
        let name = $('#authorFullName').val();
        const request = id === 0
            ? api.createAuthor(new author(name))
            : api.updateAuthor(id, new updateAuthorRequest(name));
        request
            .then(() => {
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => {
                console.error("Ошибка при сохранении автора:", error);
                alert(error);
            });
    }
});
