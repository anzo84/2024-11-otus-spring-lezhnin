import 'popper.js';
import $ from 'jquery';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'admin-lte/dist/css/adminlte.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './style.css';
import 'admin-lte/dist/js/adminlte.min.js';
import '@fortawesome/fontawesome-free/js/all.min.js';

const OtusBookLibraryApiClient = require('otus-book-library');

export const booksApi = new OtusBookLibraryApiClient.BooksApi();
export const genresApi = new OtusBookLibraryApiClient.GenresApi();
export const authorsApi = new OtusBookLibraryApiClient.AuthorsApi();
export const commentsApi = new OtusBookLibraryApiClient.CommentsApi();
export const metricsApi = new OtusBookLibraryApiClient.MetricsApi();