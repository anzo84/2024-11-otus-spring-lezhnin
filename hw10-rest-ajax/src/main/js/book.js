import $ from 'jquery';
import {genresApi, booksApi, authorsApi} from './include/apiClient';
import {reloadTable, showAlert} from "./include/common";

import modifyBook from "otus-book-library/src/model/ModifyBook";
import book from "otus-book-library/src/model/Book";

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
