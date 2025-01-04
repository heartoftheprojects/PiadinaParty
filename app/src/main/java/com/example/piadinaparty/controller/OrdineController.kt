package com.example.piadinaparty.controller

import com.google.firebase.firestore.FirebaseFirestore
import com.example.piadinaparty.model.Ordine
import com.example.piadinaparty.model.Utente

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
                // Calcola i punti
                val punti = calcolaPunti(order.prezzo)
                // Aggiorna i punti dell'utente
                val userRef = db.collection("users").document(order.userId)
                userRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(Utente::class.java)
                        if (user != null) {
                            val nuoviPunti = user.points + punti
                            userRef.update("points", nuoviPunti)
                                .addOnSuccessListener {
                                    callback(true)
                                }
                                .addOnFailureListener {
                                    callback(false)
                                }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }

    fun calcolaPunti(prezzo: Double): Int {
        return when {
            prezzo < 10 -> 2
            prezzo in 10.0..20.0 -> 3
            else -> 4
        }
    }
}