package com.example.piadinaparty.controller

import com.google.firebase.firestore.FirebaseFirestore
import com.example.piadinaparty.model.Item
import com.example.piadinaparty.model.Offerta
import com.example.piadinaparty.model.Ordine
import com.example.piadinaparty.model.Utente

class OrdineController {
    private val db = FirebaseFirestore.getInstance()

    //Questa funzione ottiene gli ordini più frequenti effettuati da un utente specifico, identificato dal suo userId, da un database di Firestore
    fun getFrequentOrders(userId: String, callback: (List<Ordine>) -> Unit) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("frequency", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get() //ottieni il resultato della query
            .addOnSuccessListener { result ->
                val orders = result.map { document ->
                    val items = (document["items"] as List<Map<String, Any>>).map { itemMap -> //estrazione dei singoli elementi ordinati con visualizzazione dei singoli campi
                        Item(
                            name = itemMap["name"] as String,
                            price = (itemMap["price"] as Double),
                            quantity = (itemMap["quantity"] as Long).toInt(),
                            description = itemMap["description"] as String
                        )
                    }
                    val offertaMap = document["offerta"] as? Map<String, Any> //estrazione dell'offerta presente nell'ordine
                    val offerta = offertaMap?.let {
                        Offerta(
                            id = it["id"] as String,
                            description = it["description"] as String,
                            price = it["price"] as Double,
                            pointsRequired = (it["pointsRequired"] as Long).toInt()
                        )
                    }
                    Ordine( //creazione oggetto ordine
                        id = document.id,
                        userId = document["userId"] as String,
                        items = items,
                        frequency = (document["frequency"] as Long).toInt(),
                        prezzo = document["prezzo"] as Double,
                        offerta = offerta
                    )
                }
                callback(orders) //se query andata buon fine, si passa la lista di ordini "orders"
            }
            .addOnFailureListener { exception ->
                callback(emptyList()) //se query non è andata buon fine, si passa la lista vuota
            }
    }

    fun addOrder(order: Ordine, callback: (Boolean) -> Unit) {
        val ordersRef = db.collection("orders")   //query per cercarti nel database gli ordini effettuati da un certo utente
        ordersRef
            .whereEqualTo("userId", order.userId)
            .get()
            .addOnSuccessListener { result ->
                val existingOrder = result.documents.find { document ->   //existingOrder sono i singoli ordini effettuati da un utente e recuperati da firebase
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
                    items == order.items && offerta == order.offerta  //confronto degli items dell'ordine presente in firebase con items del nuovo ordine e confronto l'offerta dell'ordine presente in firebase con l'offerta dell'ordine nuovo
                }

                //se non esiste nessun ordine, si aggiunge al database l'ordine passato come parametro nella funzione
                if (existingOrder == null) {
                    val orderRef = ordersRef.document() //nuovo documento nella collezione orders
                    val orderWithId = order.copy(id = orderRef.id, frequency = 1)
                    val ordineMap = hashMapOf( //mappa (ordineMap) che rappresenta i dati del nuovo ordine, formattati correttamente
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

                    orderRef.set(ordineMap) //aggiungi il nuovo ordine a firebase
                        .addOnSuccessListener {
                            // Calcola i punti
                            val punti = calcolaPunti(order.prezzo) //calcolo dei punti da assegnare all'utente in base all'ordine del prezzo
                            // Aggiorna i punti dell'utente
                            val userRef = db.collection("users").document(order.userId)
                            userRef.get().addOnSuccessListener { document ->
                                if (document != null) {
                                    val user = document.toObject(Utente::class.java)
                                    if (user != null) {
                                        val nuoviPunti = user.points + punti
                                        userRef.update("points", nuoviPunti) //recuperato il documento dell'utente dalla collezione users, si aggiorna i suoi punti totali
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
                    // Se existingOrder != null, significa che l'ordine è già presente
                    val newFrequency = (existingOrder["frequency"] as Long).toInt() + 1 //Si recupera la frequenza attuale dall'ordine e la incrementa di 1
                    ordersRef.document(existingOrder.id) //Si aggiorna la frequenza dell'ordine già presente anche nel database
                        .update("frequency", newFrequency)
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