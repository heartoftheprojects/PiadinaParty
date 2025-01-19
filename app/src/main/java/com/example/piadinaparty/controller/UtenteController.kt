package com.example.piadinaparty.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.piadinaparty.MainActivity
import com.example.piadinaparty.model.Utente
import com.example.piadinaparty.view.ActivityLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UtenteController(private val context: Context) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //funzione per effettuare controlli sul login
    fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) { //controllo se i campi non sono vuoti
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("UtenteController", "Login avvenuto correttamente!")
                        Toast.makeText(context, "Login avvenuto correttamente!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("FRAGMENT_TO_LOAD", "HOME_FRAGMENT")
                        context.startActivity(intent)
                    } else {
                        Log.d("UtenteController", "Login fallito!")
                        Toast.makeText(context, "Login fallito!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Inserire Email e Password", Toast.LENGTH_SHORT).show()
        }
    }

    //funzione per effetturare controlli sulla registrazione
    fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null) { //controllo se l'utente esiste già con le credenziali inserite
                            val userId = user.uid //assegnazione id generato da firebase all'utente nuovo
                            val newUser = Utente( //creazione oggetto della classe utente con i valori dei campi inseriti
                                id = userId,
                                firstName = firstName,
                                lastName = lastName,
                                email = email
                            )

                            firestore.collection("users").document(userId).set(newUser) //inserimento del nuovo oggetto utente nella raccolta users
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, ActivityLogin::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirestoreError", "Errore nel salvataggio dei dati", e)
                                    Toast.makeText(context, "Errore nel salvataggio dei dati: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(context, "Registrazione non avvenuta con successo", Toast.LENGTH_SHORT).show() //se le credenziali dell'utente esistono già
                    }
                }
        } else {
            Toast.makeText(context, "Compila tutti i campi", Toast.LENGTH_SHORT).show() //se non tutti i campi sono riempiti
        }
    }

    //funzione per recuperare dal database le informazioni relative all'utente da visualizzare nel fragmentUtenti
    fun fetchUserData(onUserDataFetched: (Utente?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(Utente::class.java)
                        Log.d("UserController", "User data fetched: $user")
                        onUserDataFetched(user)
                    } else {
                        Log.d("UserController", "No such document")
                        onUserDataFetched(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("UserController", "Error getting document: ", exception)
                    onUserDataFetched(null)
                }
        } else {
            Log.d("UserController", "No current user")
            onUserDataFetched(null)
        }
    }

    fun logoutUser() {   //funzione del logout
        firebaseAuth.signOut()
    }

    //funzione per recuperare punti dell'utente dal database
    fun getUserPoints(userId: String, callback: (Int?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(Utente::class.java)
                    Log.d("UserController", "User points fetched: ${user?.points}")
                    callback(user?.points)
                } else {
                    Log.d("UserController", "No such document")
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("UserController", "Error getting document: ", exception)
                callback(null)
            }
    }

    //funzione per aggiornare sul database i punti dell'utente in caso di offerta utilizzata
    fun updateUserPoints(userId: String, newPoints: Int, callback: (Boolean) -> Unit) {
        firestore.collection("users").document(userId).update("points", newPoints)
            .addOnSuccessListener {
                Log.d("UserController", "User points updated to $newPoints")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.w("UserController", "Error updating points: ", exception)
                callback(false)
            }
    }
}
