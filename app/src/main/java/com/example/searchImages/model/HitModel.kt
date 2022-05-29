package com.example.searchImages.model

data class HitModel(
    val id: Double,
    val pageURL: String,
    val type: String,
    val tags: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int,
    val webformatHeight: Int,
    val largeImageURL: String,
    val fullHDURL: String,
    val imageURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val user_id: Double,
    val user: String,
    val userImageURL: String
)

data class ResponseHitsModel(
    val total: Int,
    val totalHits: Int,
    val hits: List<HitModel>
)