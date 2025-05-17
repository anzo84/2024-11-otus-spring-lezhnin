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
const OtusBookLibraryApiClient = require('otus-book-library');

export const booksApi = new OtusBookLibraryApiClient.BooksApi();
export const genresApi = new OtusBookLibraryApiClient.GenresApi();
export const authorsApi = new OtusBookLibraryApiClient.AuthorsApi();
export const commentsApi = new OtusBookLibraryApiClient.CommentsApi();
export const metricsApi = new OtusBookLibraryApiClient.MetricsApi();
export const usersApi = new OtusBookLibraryApiClient.UsersApi();