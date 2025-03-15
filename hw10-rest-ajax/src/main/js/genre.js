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
import {reloadTable, showAlert} from "./include/common";

window.$ = $;
window.jQuery = $;

const OtusBookLibraryApiClient = require('otus-book-library');
const api = new OtusBookLibraryApiClient.GenresApi();

async function reloadGenreList(tableBodyId, editDialogId) {
    try {
        const genres = await api.getAllGenres();
        reloadTable(tableBodyId, editDialogId, genres, [
            genre => genre.name
        ]);
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
