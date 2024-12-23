package com.example.piadinaparty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.databinding.ActivityRegistrazioneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class activityRegistrazione : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrazioneBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.buttonConferma.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val nome = binding.nome.text.toString()
            val cognome = binding.cognome.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && nome.isNotEmpty() && cognome.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        user?.let {
                            val userId = it.uid
                            val userMap = hashMapOf(
                                "firstName" to nome,
                                "lastName" to cognome,
                                "email" to email
                            )

                            firestore.collection("users").document(userId).set(userMap).addOnSuccessListener {
                                Toast.makeText(this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, activityLogin::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener { e ->
                                Toast.makeText(this, "Errore nel salvataggio dei dati: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Registrazione non avvenuta con successo", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this,activityLogin::class.java))
            finish()
        }
    }
}
