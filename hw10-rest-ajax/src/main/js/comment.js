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

import updateCommentRequest from "otus-book-library/src/model/UpdateCommentRequest";
import comment from "otus-book-library/src/model/Comment";

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
            $('#actionBtn-saveDialog').hide();
            $('#emptyBooksInfo').hide();
            $("#commentList").parent().hide();
        } else {
            $('#actionBtn-saveDialog').show();
            reloadCommentList(this.value, "commentList", "saveDialog").then();
        }
    });
}

async function reloadCommentList(bookId, tableBodyId, editDialogId) {
    try {
        let tableBody = $("#" + tableBodyId);
        const comments = await commentsApi.getAllComments(bookId);
        $('#emptyBooksInfo').hide();

        if (Array.isArray(comments) && comments.length === 0) {
            $('#emptyCommentsInfo').show();
            tableBody.parent().hide();
        } else {
            $('#emptyCommentsInfo').hide();
            tableBody.parent().show();
            tableBody.empty();

            let i = 1;
            comments.forEach(comment => {
                const newRow = $("<tr>");
                const buttonEdit = $("<button>")
                    .attr("id", "edit" + tableBodyId + i)
                    .attr("role", "button")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#" + editDialogId)
                    .attr("data-action", tableBody.data("edit-action"))
                    .attr("data-title", tableBody.data("edit-title"))
                    .attr("data-param", comment.id)
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
                    .attr("data-param", comment.id)
                    .append($("<span>")
                        .addClass("fas")
                        .addClass("fa-trash-can"));

                newRow.append(
                    $("<td>").text(i++),
                    $("<td>").text(comment.content),
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
    } else if (action === "newCommentAction") {
        $("#commentId").val(0);
        $('#commentContent').val("");
    } else if (action === "saveCommentAction") {
        const id = Number.parseInt($('#commentId').val());
        const content = $('#commentContent').val();
        booksApi.getBookById($("#book").val()).then(book => {
            const request = id === 0
                ? commentsApi.createComment(new comment(content, book))
                : commentsApi.updateComment(id, new updateCommentRequest(content, book));
            request
                .then(() => {
                    $("#book").change();
                    $('#saveDialog').modal('hide');
                })
                .catch(error => {
                    console.error("Ошибка при сохранении книги:", error);
                    alert(error);
                });
        });
    }
});
