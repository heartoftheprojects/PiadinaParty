package com.example.piadinaparty.controller

import com.google.firebase.firestore.FirebaseFirestore
import com.example.piadinaparty.model.Ordine

class OrdineController {
    private val db = FirebaseFirestore.getInstance()

    fun getFrequentOrders(userId: String, callback: (List<Ordine>) -> Unit) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("frequency", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val orders = result.map { document ->
                    document.toObject(Ordine::class.java).apply {
                        id = document.id
                    }
                }
                callback(orders)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
            }
    }

    fun addOrder(order: Ordine, callback: (Boolean) -> Unit) {
        val orderRef = db.collection("orders").document()
        order.id = orderRef.id
        orderRef.set(order)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }
}