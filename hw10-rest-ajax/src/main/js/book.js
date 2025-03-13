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

import updateBookRequest from "otus-book-library/src/model/UpdateBookRequest";
import book from "otus-book-library/src/model/Book";

const OtusBookLibraryApiClient = require('api__');
const booksApi = new OtusBookLibraryApiClient.BooksApi();
const genresApi = new OtusBookLibraryApiClient.GenresApi();
const authorsApi = new OtusBookLibraryApiClient.AuthorsApi();

async function reloadBookList(tableBodyId, editDialogId) {
    try {
        let tableBody = $("#" + tableBodyId);
        const books = await booksApi.getAllBooks();
        if (Array.isArray(books) && books.length === 0) {
            $('#emptyInfo').show();
            tableBody.parent().hide();
        } else {
            $('#emptyInfo').hide();
            tableBody.parent().show();
            tableBody.empty();

            let i = 1;
            books.forEach(book => {
                const newRow = $("<tr>");
                const buttonEdit = $("<button>")
                    .attr("id", "edit" + tableBodyId + i)
                    .attr("role", "button")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#" + editDialogId)
                    .attr("data-action", tableBody.data("edit-action"))
                    .attr("data-title", tableBody.data("edit-title"))
                    .attr("data-param", book.id)
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
                    .attr("data-param", book.id)
                    .append($("<span>")
                        .addClass("fas")
                        .addClass("fa-trash-can"));

                const genres = book.genres.map(genre => genre.name).join(', ');

                newRow.append(
                    $("<td>").text(i++),
                    $("<td>").text(book.title),
                    $("<td>").text(book.author.fullName),
                    $("<td>").text(genres),
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
    reloadBookList("bookList", "saveDialog").then();
}

function loadAuthors() {
    authorsApi.getAllAuthors().then(authors => {
        let bookAuthor = $("#bookAuthor");
        bookAuthor
            .empty()
            .append($("<option>")
                .attr("selected", true)
                .val(null)
                .text(bookAuthor.data("not-selected")));

        authors.forEach(author => {
            bookAuthor
                .append($("<option>")
                    .val(author.id)
                    .text(author.fullName));
        });
    });
}

function loadGenres() {
    let bookGenres = $('#bookGenres');
    genresApi.getAllGenres().then(genres => {
        genres.forEach(genre => {
            bookGenres
                .append($("<option>")
                    .val(genre.id)
                    .text(genre.name));
        });
    });
}

$(document).ready(function () {
    $(".select2").select2({
        dropdownParent: $('#saveDialog')
    });
    reload();
    loadAuthors();
    loadGenres();
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteBookAction") {
        booksApi.deleteBook(Number.parseInt($(this).data("param")))
            .then(() => {
                reload();
            });
    } else if (action === "editBookAction") {
        const id = Number.parseInt($(this).data("param"));
        booksApi.getBookById(id).then(book => {
            $('#bookId').val(book.id);
            $('#bookTitle').val(book.title);
            $('#bookAuthor').val(book.author.id).change();
            $("#bookGenres").val(book.genres.map(g => g.id)).change();
        });
    } else if (action === "newBookAction") {
        $("#bookId").val(0);
        $("#bookTitle, #bookAuthor, #bookGenres").val("").change();
    } else if (action === "saveBookAction") {
        const id = Number.parseInt($('#bookId').val());
        const title = $('#bookTitle').val();
        const author = {"id": $('#bookAuthor').val(), "fullName": "_"};
        const genres = $('#bookGenres').val().map(i => ({id: i, name: "_"}));
        const request = id === 0
            ? booksApi.createBook(new book(title, author, genres))
            : booksApi.updateBook(id, new updateBookRequest(title, author, genres));
        request
            .then(() => {
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => {
                console.error("Ошибка при сохранении книги:", error);
                alert(error);
            });
    }
});
