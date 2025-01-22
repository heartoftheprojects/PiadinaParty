package com.example.piadinaparty.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.controller.UtenteController
import com.example.piadinaparty.databinding.ActivityRegistrazioneBinding

class ActivityRegistrazione : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrazioneBinding
    private lateinit var userController: UtenteController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userController = UtenteController(this) //crea un'istanza del controller associandola all'activity

        binding.buttonConferma.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val nome = binding.nome.text.toString()
            val cognome = binding.cognome.text.toString()

            userController.registerUser(nome, cognome, email, password)
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, ActivityLogin::class.java))
            finish()
        }
    }
}