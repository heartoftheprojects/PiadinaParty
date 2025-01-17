package com.example.piadinaparty.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.controller.UtenteController
import com.example.piadinaparty.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userController: UtenteController

    //Configurazione activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userController = UtenteController(this)

        //caricamento di email e password inseriti
        binding.loginButton.setOnClickListener {
            val email = binding.LoginEmail.text.toString()
            val password = binding.LoginPassword.text.toString()

            //email e password inseriti passati come parametri alla funzione loginUser del controller utente
            userController.loginUser(email, password)
        }

        binding.Messaggioschermo1.setOnClickListener {
            startActivity(Intent(this, ActivityRegistrazione::class.java))
            finish()
        }
    }
}