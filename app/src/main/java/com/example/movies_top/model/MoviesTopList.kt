package com.example.movies_top.model

data class MoviesTopList(
    val films: List<Film>,
    val pagesCount: Int
) {
    data class Film(
        val countries: List<Country>,
        val filmId: Long,
        val filmLength: String,
        val genres: List<Genre>,
        val nameEn: String,
        val nameRu: String,
        val posterUrl: String,
        val posterUrlPreview: String,
        val rating: String,
        val ratingChange: Any,
        val ratingVoteCount: Int,
        val year: String
    )
}