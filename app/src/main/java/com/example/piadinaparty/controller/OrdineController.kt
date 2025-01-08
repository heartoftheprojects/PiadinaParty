package com.example.piadinaparty.controller

import com.google.firebase.firestore.FirebaseFirestore
import com.example.piadinaparty.model.Item
import com.example.piadinaparty.model.Offerta
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
                    val offertaMap = document["offerta"] as? Map<String, Any>
                    val offerta = offertaMap?.let {
                        Offerta(
                            id = it["id"] as String,
                            description = it["description"] as String,
                            price = it["price"] as Double,
                            pointsRequired = (it["pointsRequired"] as Long).toInt()
                        )
                    }
                    Ordine(
                        id = document.id,
                        userId = document["userId"] as String,
                        items = items,
                        frequency = (document["frequency"] as Long).toInt(),
                        prezzo = document["prezzo"] as Double,
                        offerta = offerta
                    )
                }
                callback(orders)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
            }
    }

    fun addOrder(order: Ordine, callback: (Boolean) -> Unit) {
        val ordersRef = db.collection("orders")
        ordersRef
            .whereEqualTo("userId", order.userId)
            .get()
            .addOnSuccessListener { result ->
                val existingOrder = result.documents.find { document ->
                    val items = (document["items"] as List<Map<String, Any>>).map { itemMap ->
                        Item(
                            name = itemMap["name"] as String,
                            price = (itemMap["price"] as Double),
                            quantity = (itemMap["quantity"] as Long).toInt(),
                            description = itemMap["description"] as String
                        )
                    }
                    val offertaMap = document["offerta"] as? Map<String, Any>
                    val offerta = offertaMap?.let {
                        Offerta(
                            id = it["id"] as String,
                            description = it["description"] as String,
                            price = it["price"] as Double,
                            pointsRequired = (it["pointsRequired"] as Long).toInt()
                        )
                    }
                    items == order.items && offerta == order.offerta
                }

                if (existingOrder == null) {
                    // Ordine non esiste, aggiungi nuovo ordine
                    val orderRef = ordersRef.document()
                    val orderWithId = order.copy(id = orderRef.id, frequency = 1)
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
                        "prezzo" to orderWithId.prezzo,
                        "offerta" to orderWithId.offerta?.let { offerta ->
                            hashMapOf(
                                "id" to offerta.id,
                                "description" to offerta.description,
                                "price" to offerta.price,
                                "pointsRequired" to offerta.pointsRequired
                            )
                        }
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
                } else {
                    // Ordine esiste, incrementa la frequenza
                    val newFrequency = (existingOrder["frequency"] as Long).toInt() + 1
                    ordersRef.document(existingOrder.id)
                        .update("frequency", newFrequency)
                        .addOnSuccessListener {
                            callback(true)
                        }
                        .addOnFailureListener {
                            callback(false)
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