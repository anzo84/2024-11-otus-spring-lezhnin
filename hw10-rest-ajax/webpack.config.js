const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: {
        error: './src/main/js/error.js',
        home: './src/main/js/home.js',
        book: './src/main/js/book.js',
        genre: './src/main/js/genre.js',
        author: './src/main/js/author.js',
        comment: './src/main/js/comment.js'
    },
    output: {
        filename: '[name].bundle.js',
        path: path.resolve(__dirname, 'src/main/resources/static/js')
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(scss)$/,
                use: ['style-loader', 'css-loader', 'sass-loader'],
            },
            {
                parser: {
                    amd: false
                }
            },
            {
                test: /\.(png|jpe?g|gif|svg|woff|woff2|eot|ttf|otf)$/i,
                type: 'asset/resource',
            },
        ]
    },
    resolve: {
        extensions: ['.js'],
        alias: {
            'otus-book-library': path.resolve(__dirname, 'src/main/js/otus-book-library')
        }
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery'
        })
    ],
    mode: 'development'
};