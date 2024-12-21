package com.example.piadinaparty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class activityProfiloUtente : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo_utente)



        // Riferimento al bottone "Indietro"
        val buttonIndietro: Button = findViewById(R.id.button7)

        // Imposta il click listener per tornare a activitySchermata2
        buttonIndietro.setOnClickListener {
            val intent = Intent(this, activityHome::class.java)
            startActivity(intent)
            finish() // Termina l'activity corrente
        }
    }
}
