import $ from 'jquery';
import {usersApi} from './include/apiClient';
import {loadTable, showAlert, loadOptions} from "./include/common";

import ModifyUser from "otus-book-library/src/model/ModifyUser";
import User from "otus-book-library/src/model/User";

async function reloadUsersWithParams(tableBodyId, editDialogId) {
    try {
        const users = await usersApi.getAllUsers();
        loadTable(tableBodyId, editDialogId, users, [
            user => user.username,
            user => user.roles.join(', ')
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
    loadOptions($("#userRoles"), usersApi.getAllRoles(),
            r => r.role, r => r.description);
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
            console.log("user="+user)
            $('#userId').val(user.id);
            $('#userName').val(user.username);
            $('#userPassword').val(user.password);
            $('#userRoles').val(user.roles).change();
        });
    } else if (action === "saveUserAction") {
        const id = Number.parseInt($('#userId').val());
        const userName = $('#userName').val();
        const userPassword = $('#userPassword').val();
        const roles = $('#userRoles').val();
        const request = id === 0
            ? usersApi.createUser(new User(userName, userPassword, roles))
            : usersApi.updateUser(id, new ModifyUser(userName, userPassword, roles));
        request
            .then(() => {
                reloadUsers();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
