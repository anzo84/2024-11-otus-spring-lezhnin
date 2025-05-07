import $ from 'jquery';
import {commentsApi, booksApi} from './include/apiClient';
import {loadOptions, loadTable, showAlert} from "./include/common";

import ModifyComment from "otus-book-library/src/model/ModifyComment";
import Comment from "otus-book-library/src/model/Comment";

async function reloadCommentsWithParams(bookId, tableBodyId, editDialogId) {
    try {
        const comments = await commentsApi.getAllComments(bookId);
        loadTable(tableBodyId, editDialogId, comments, [
            comment => comment.content
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

$(document).ready(function () {
    $(".select2").select2();

    const bookSelect = $("#book");
    loadOptions(bookSelect, booksApi.getAllBooks(),
        b => b.id, b => `${b.title} (${b.author.fullName})`);

    bookSelect.on("change", function () {
        const hasOptions = $(this).find('option').length > 1;
        $('#emptyBooksInfo').toggle(!hasOptions);
        $('#selectBookForm').toggle(hasOptions);
        if (this.value === null || this.value === "") {
            $('#actionBtn-saveDialog, #emptyBooksInfo').hide();
            $("#commentList").parent().hide();
        } else {
            $('#actionBtn-saveDialog').show();
            reloadCommentsWithParams(this.value, "commentList", "saveDialog").then();
        }
    });
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteCommentAction") {
        commentsApi.deleteComment(Number.parseInt($(this).data("param")))
            .then(() => {
                $("#book").change();
            });
    } else if (action === "editCommentAction") {
        const id = Number.parseInt($(this).data("param"));
        commentsApi.getCommentById(id).then(comment => {
            $('#commentId').val(comment.id);
            $('#commentContent').val(comment.content);
        });
    } else if (action === "saveCommentAction") {
        const id = Number.parseInt($('#commentId').val());
        const content = $('#commentContent').val();
        booksApi.getBookById($("#book").val()).then(book => {
            const request = id === 0
                ? commentsApi.createComment(new Comment(content, book))
                : commentsApi.updateComment(id, new ModifyComment(content, book));
            request
                .then(() => {
                    $("#book").change();
                    $('#saveDialog').modal('hide');
                })
                .catch(error => showAlert(error));
        });
    }
});
