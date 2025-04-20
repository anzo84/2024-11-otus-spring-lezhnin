import $ from 'jquery';
import {genresApi, usersApi, authorsApi} from './include/apiClient';
import {loadTable, showAlert, loadOptions} from "./include/common";

import modifyUser from "otus-user-library/src/model/ModifyUser";
import user from "otus-user-library/src/model/User";

async function reloadUsersWithParams(tableBodyId, editDialogId) {
    try {
        const users = await usersApi.getAllUsers();
        loadTable(tableBodyId, editDialogId, users, [
            user => user.title,
            user => user.author.fullName,
            user => user.genres.map(genre => genre.name).join(', ')
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

function reloadUsers() {
    reloadUsersWithParams("userList", "saveDialog").then();
}

$(document).ready(function () {
    $(".select2").select2({
        dropdownParent: $('#saveDialog')
    });
    reloadUsers();
    loadOptions($("#userAuthor"), authorsApi.getAllAuthors(),
            a => a.id, a => a.fullName);
    loadOptions($("#userGenres"), genresApi.getAllGenres(),
            g => g.id, g => g.name);
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteUserAction") {
        usersApi.deleteUser(Number.parseInt($(this).data("param")))
            .then(() => {
                reloadUsers();
            });
    } else if (action === "editUserAction") {
        const id = Number.parseInt($(this).data("param"));
        usersApi.getUserById(id).then(user => {
            $('#userId').val(user.id);
            $('#userTitle').val(user.title);
            $('#userAuthor').val(user.author.id).change();
            $("#userGenres").val(user.genres.map(g => g.id)).change();
        });
    } else if (action === "saveUserAction") {
        const id = Number.parseInt($('#userId').val());
        const title = $('#userTitle').val();
        const author = {"id": $('#userAuthor').val(), "fullName": "_"};
        const genres = $('#userGenres').val().map(i => ({id: i, name: "_"}));
        const request = id === 0
            ? usersApi.createUser(new user(title, author, genres))
            : usersApi.updateUser(id, new modifyUser(title, author, genres));
        request
            .then(() => {
                reloadUsers();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
