package com.example.gallery

object AlbumConverter {
    @JvmStatic
    fun getAlbum(list: List<String>): List<Pair<String, List<String>>> {
        val album = list.groupBy { it.substringBeforeLast("/") }
        return album.toList()
    }
}