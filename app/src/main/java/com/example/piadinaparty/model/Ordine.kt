package com.example.piadinaparty.model

data class Ordine (
    var id: String = "",
    val userId: String = "",
    val items: List<Item> = emptyList(),
    val frequency: Int = 0
)