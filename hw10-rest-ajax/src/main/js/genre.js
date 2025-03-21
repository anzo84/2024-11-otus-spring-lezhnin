import $ from 'jquery';
import {genresApi} from './include/apiClient';
import {loadTable, showAlert} from "./include/common";

import modifyGenre from "otus-book-library/src/model/ModifyGenre";
import genre from "otus-book-library/src/model/Genre";

async function reloadGenresWithParams(tableBodyId, editDialogId) {
    try {
        const genres = await genresApi.getAllGenres();
        loadTable(tableBodyId, editDialogId, genres, [
            genre => genre.name
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

function reloadGenres() {
    reloadGenresWithParams("genreList", "saveDialog").then();
}

$(document).ready(function () {
    reloadGenres();
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteGenreAction") {
        genresApi.deleteGenre(Number.parseInt($(this).data("param")))
            .then(() => {
                reloadGenres();
            });
    } else if (action === "editGenreAction") {
        const id = Number.parseInt($(this).data("param"));
        genresApi.getGenreById(id).then(genre => {
            $('#genreId').val(id);
            $('#genreName').val(genre.name);
        });
    } else if (action === "saveGenreAction") {
        let id = Number.parseInt($('#genreId').val());
        let name = $('#genreName').val();
        const response = id === 0
            ? genresApi.createGenre(new genre(name))
            : genresApi.updateGenre(id, new modifyGenre(name));
        response
            .then(() => {
                reloadGenres();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
