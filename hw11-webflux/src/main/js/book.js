import $ from 'jquery';
import {genresApi, booksApi, authorsApi} from './include/apiClient';
import {loadTable, showAlert, loadOptions} from "./include/common";

import modifyBook from "otus-book-library/src/model/ModifyBook";
import book from "otus-book-library/src/model/Book";

async function reloadBooksWithParams(tableBodyId, editDialogId) {
    try {
        const books = await booksApi.getAllBooks();
        loadTable(tableBodyId, editDialogId, books, [
            book => book.title,
            book => book.author.fullName,
            book => book.genres.map(genre => genre.name).join(', ')
        ]);
    } catch (err) {
        console.error("Ошибка:", err);
    }
}

function reloadBooks() {
    reloadBooksWithParams("bookList", "saveDialog").then();
}

$(document).ready(function () {
    $(".select2").select2({
        dropdownParent: $('#saveDialog')
    });
    reloadBooks();
    loadOptions($("#bookAuthor"), authorsApi.getAllAuthors(),
            a => a.id, a => a.fullName);
    loadOptions($("#bookGenres"), genresApi.getAllGenres(),
            g => g.id, g => g.name);
});

$('body').on('click', 'button', function () {
    let action = $(this).data("action");
    if (action === "deleteBookAction") {
        booksApi.deleteBook(Number.parseInt($(this).data("param")))
            .then(() => {
                reloadBooks();
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
                reloadBooks();
                $('#saveDialog').modal('hide');
            })
            .catch(error => showAlert(error));
    }
});
