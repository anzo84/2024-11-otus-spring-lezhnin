import 'popper.js';
import $ from 'jquery';
import select2 from 'select2';

select2($);

window.$ = $;
window.jQuery = $;

import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'admin-lte/dist/css/adminlte.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'select2/dist/css/select2.min.css';
import '@ttskch/select2-bootstrap4-theme/dist/select2-bootstrap4.min.css'
import 'admin-lte/dist/js/adminlte.min.js';
import '@fortawesome/fontawesome-free/js/all.min.js';
import './style.css';

import modifyComment from "otus-book-library/src/model/ModifyComment";
import comment from "otus-book-library/src/model/Comment";
import {reloadTable, showAlert} from "./include/common";

const OtusCommentLibraryApiClient = require('otus-book-library');
const commentsApi = new OtusCommentLibraryApiClient.CommentsApi();
const booksApi = new OtusCommentLibraryApiClient.BooksApi();

function loadBooks() {
    const bookSelect = $("#book");

    booksApi.getAllBooks().then(books => {
        bookSelect
            .empty()
            .append($("<option>")
                .attr("selected", true)
                .val(0)
                .text(bookSelect.data("not-selected")));

        $('#emptyCommentsInfo').hide();
        $('#commentList').parent().hide();
        $('#actionBtn-saveDialog').hide();

        if (Array.isArray(books) && books.length === 0) {
            $('#emptyBooksInfo').show();
            $('#selectBookForm').hide();
        } else {
            $('#emptyBooksInfo').hide();
            $('#selectBookForm').show();
            books.forEach(book => {
                bookSelect
                    .append($("<option>")
                        .val(book.id)
                        .text(`${book.title} (${book.author.fullName})`));
            });
        }
    });

    bookSelect.on("change", function () {
        if (this.value === "0") {
            $('#actionBtn-saveDialog, #emptyBooksInfo').hide();
            $("#commentList").parent().hide();
        } else {
            $('#actionBtn-saveDialog').show();
            reloadCommentList(this.value, "commentList", "saveDialog").then();
        }
    });
}

async function reloadCommentList(bookId, tableBodyId, editDialogId) {
    try {
        const comments = await commentsApi.getAllComments(bookId);
        reloadTable(tableBodyId, editDialogId, comments, [
            comment => comment.content
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

$(document).ready(function () {
    $(".select2").select2();
    loadBooks();
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
                ? commentsApi.createComment(new comment(content, book))
                : commentsApi.updateComment(id, new modifyComment(content, book));
            request
                .then(() => {
                    $("#book").change();
                    $('#saveDialog').modal('hide');
                })
                .catch(error => showAlert(error));
        });
    }
});
