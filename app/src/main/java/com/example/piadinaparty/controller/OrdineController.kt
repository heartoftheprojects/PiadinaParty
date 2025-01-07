package com.example.piadinaparty.controller

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.piadinaparty.model.Item
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
                    val items = (document["items"] as List<Map<String, Any>>).map { itemMap ->
                        Item(
                            name = itemMap["name"] as String,
                            price = (itemMap["price"] as Double),
                            quantity = (itemMap["quantity"] as Long).toInt(),
                            description = itemMap["description"] as String
                        )
                    }
                    Ordine(
                        id = document.id,
                        userId = document["userId"] as String,
                        items = items,
                        frequency = (document["frequency"] as Long).toInt(),
                        prezzo = document["prezzo"] as Double
                    )
                }
                callback(orders)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
            }
    }

    fun addOrder(order: Ordine, callback: (Boolean) -> Unit) {
        val orderRef = db.collection("orders").document()
        val orderWithId = order.copy(id = orderRef.id)
        val ordineMap = hashMapOf(
            "id" to orderWithId.id,
            "userId" to orderWithId.userId,
            "items" to orderWithId.items.map { item ->
                hashMapOf(
                    "name" to item.name,
                    "price" to item.price,
                    "quantity" to item.quantity,
                    "description" to item.description
                )
            },
            "frequency" to orderWithId.frequency,
            "prezzo" to orderWithId.prezzo
        )

        orderRef.set(ordineMap)
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

    fun printOrders(userId: String) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("frequency", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val items = (document["items"] as List<Map<String, Any>>).map { itemMap ->
                        Item(
                            name = itemMap["name"] as String,
                            price = (itemMap["price"] as Double),
                            quantity = (itemMap["quantity"] as Long).toInt(),
                            description = itemMap["description"] as String
                        )
                    }
                    val order = Ordine(
                        id = document.id,
                        userId = document["userId"] as String,
                        items = items,
                        frequency = (document["frequency"] as Long).toInt(),
                        prezzo = document["prezzo"] as Double
                    )
                    // Formatta i dati dell'ordine come stringa leggibile
                    val orderDetails = """
                        Order ID: ${order.id}
                        User ID: ${order.userId}
                        Items: ${order.items.joinToString { "${it.name} (x${it.quantity})" }}
                        Frequency: ${order.frequency}
                        Price: €${order.prezzo}
                    """.trimIndent()
                    Log.d("OrdineController", orderDetails)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("OrdineController", "Error getting documents: ", exception)
            }
    }
}