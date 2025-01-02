package com.example.piadinaparty.model

data class Item(
    val name: String,
    val price: Double,
    var quantity: Int = 0,
    val description: String
)