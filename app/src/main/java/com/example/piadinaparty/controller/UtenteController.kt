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

    fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login avvenuto correttamente!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("FRAGMENT_TO_LOAD", "HOME_FRAGMENT")
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Login fallito!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Inserire Email e Password", Toast.LENGTH_SHORT).show()
        }
    }

    fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            val userId = user.uid
                            val newUser = Utente(
                                id = userId,
                                firstName = firstName,
                                lastName = lastName,
                                email = email
                            )

                            firestore.collection("users").document(userId).set(newUser)
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
                        Toast.makeText(context, "Registrazione non avvenuta con successo", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchUserData(onUserDataFetched: (Utente?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(Utente::class.java)
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
            onUserDataFetched(null)
        }
    }

    fun getUserPoints(userId: String, callback: (Int?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(Utente::class.java)
                    callback(user?.points)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun updateUserPoints(userId: String, newPoints: Int, callback: (Boolean) -> Unit) {
        firestore.collection("users").document(userId).update("points", newPoints)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}
