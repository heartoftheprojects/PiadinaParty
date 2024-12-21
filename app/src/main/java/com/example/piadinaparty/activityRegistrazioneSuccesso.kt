package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class activityRegistrazioneSuccesso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione_successo)

        val imageView = findViewById<ImageView>(R.id.imageView1)
        val textView = findViewById<TextView>(R.id.Messaggio)

        // Impostazione dell'immagine
        imageView.setImageResource(R.drawable.piadinapartylogo)

        // Impostazione del testo del TextView
        textView.text = "Registrazione avvenuta con successo"

        val btnBack = findViewById<Button>(R.id.Ritorno)

        btnBack.setOnClickListener {
            val intent = Intent(this, activityLogin::class.java)
            startActivity(intent)}
    }
}