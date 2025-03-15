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

import modifyBook from "otus-book-library/src/model/ModifyBook";
import book from "otus-book-library/src/model/Book";
import {showAlert, reloadTable} from "./include/common";

const OtusBookLibraryApiClient = require('otus-book-library');
const booksApi = new OtusBookLibraryApiClient.BooksApi();
const genresApi = new OtusBookLibraryApiClient.GenresApi();
const authorsApi = new OtusBookLibraryApiClient.AuthorsApi();

async function reloadBookList(tableBodyId, editDialogId) {
    try {
        const books = await booksApi.getAllBooks();
        reloadTable(tableBodyId, editDialogId, books, [
            book => book.title,
            book => book.author.fullName,
            book => book.genres.map(genre => genre.name).join(', ')
        ]);
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
    } else if (action === "saveBookAction") {
        const id = Number.parseInt($('#bookId').val());
        const title = $('#bookTitle').val();
        const author = {"id": $('#bookAuthor').val(), "fullName": "_"};
        const genres = $('#bookGenres').val().map(i => ({id: i, name: "_"}));
        const request = id === 0
            ? booksApi.createBook(new book(title, author, genres))
            : booksApi.updateBook(id, new modifyBook(title, author, genres));
        request
            .then(() => {
                reload();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
