import {genresApi} from './include/apiClient';
import {reloadTable, showAlert} from "./include/common";
import modifyGenre from "otus-book-library/src/model/ModifyGenre";
import genre from "otus-book-library/src/model/Genre";
import $ from 'jquery';

window.$ = $;

async function reloadGenreList(tableBodyId, editDialogId) {
    try {
        const genres = await genresApi.getAllGenres();
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
        genresApi.deleteGenre(Number.parseInt($(this).data("param")))
            .then(() => {
                reload();
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
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
