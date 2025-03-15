import 'popper.js';
import $ from 'jquery';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'admin-lte/dist/css/adminlte.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './style.css';
import 'admin-lte/dist/js/adminlte.min.js';
import '@fortawesome/fontawesome-free/js/all.min.js';

import modifyGenre from "otus-book-library/src/model/ModifyGenre";
import genre from "otus-book-library/src/model/Genre";
import {createDeleteButton, createEditButton, showAlert} from "./include/common";

window.$ = $;
window.jQuery = $;

const OtusBookLibraryApiClient = require('otus-book-library');
const api = new OtusBookLibraryApiClient.GenresApi();

async function reloadGenreList(tableBodyId, editDialogId) {
    try {
        let tableBody = $("#" + tableBodyId);
        const genres = await api.getAllGenres();
        if (Array.isArray(genres) && genres.length === 0) {
            $('#emptyInfo').show();
            tableBody.parent().hide();
        } else {
            $('#emptyInfo').hide();
            tableBody.parent().show();
            tableBody.empty();

            let i = 1;
            genres.forEach(genre => {
                const newRow = $("<tr>");
                const buttonEdit = createEditButton(editDialogId, tableBody, genre.id);
                const buttonDelete = createDeleteButton(tableBody, genre.id);

                newRow.append(
                    $("<td>").text(i++),
                    $("<td>").text(genre.name),
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
    reloadGenreList("genreList", "saveDialog").then();
}

$(document).ready(function () {
    reload();
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteGenreAction") {
        api.deleteGenre(Number.parseInt($(this).data("param")))
            .then(() => {
                reload();
            });
    } else if (action === "editGenreAction") {
        const id = Number.parseInt($(this).data("param"));
        api.getGenreById(id).then(genre => {
            $('#genreId').val(id);
            $('#genreName').val(genre.name);
        });
    } else if (action === "saveGenreAction") {
        let id = Number.parseInt($('#genreId').val());
        let name = $('#genreName').val();
        const response = id === 0
            ? api.createGenre(new genre(name))
            : api.updateGenre(id, new modifyGenre(name));
        response
            .then(() => {
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
