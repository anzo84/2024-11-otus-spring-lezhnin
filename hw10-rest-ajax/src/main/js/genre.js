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

import updateGenreRequest from "otus-book-library/src/model/UpdateGenreRequest";
import genre from "otus-book-library/src/model/Genre";

const OtusBookLibraryApiClient = require('api__');
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
                const buttonEdit = $("<button>")
                    .attr("id", "edit" + tableBodyId + i)
                    .attr("role", "button")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#" + editDialogId)
                    .attr("data-action", tableBody.data("edit-action"))
                    .attr("data-title", tableBody.data("edit-title"))
                    .attr("data-param", genre.id)
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
                    .attr("data-param", genre.id)
                    .append($("<span>")
                        .addClass("fas")
                        .addClass("fa-trash-can"));

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
    } else if (action === "newGenreAction") {
        $('#genreId').val(0);
        $('#genreName').val("");
    } else if (action === "saveGenreAction") {
        let id = Number.parseInt($('#genreId').val());
        let name = $('#genreName').val();
        const request = id === 0
            ? api.createGenre(new genre(name))
            : api.updateGenre(id, new updateGenreRequest(name));
        request
            .then(() => {
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => {
                console.error("Ошибка при сохранении жанра:", error);
                alert(error);
            });
    }
});
