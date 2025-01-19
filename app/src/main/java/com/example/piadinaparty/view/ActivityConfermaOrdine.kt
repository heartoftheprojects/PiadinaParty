package com.example.piadinaparty.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.piadinaparty.MainActivity
import com.example.piadinaparty.R

class ActivityConfermaOrdine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confermaordine)

        // Trova i componenti nel layout
        val imageView = findViewById<ImageView>(R.id.imageView2)
        val textView = findViewById<TextView>(R.id.textView5)
        val buttonChiudi = findViewById<Button>(R.id.button5)
        val buttonNuovoOrdine = findViewById<Button>(R.id.button6)

        // Impostazione dell'immagine
        imageView.setImageResource(R.drawable.piadinapartylogo)

        // Impostazione del testo del TextView
        textView.text = "Il tuo ordine è stato effettuato con successo"

        // Gestione il click del bottone "Chiudi"
        buttonChiudi.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Gestione il click del bottone "Effettua un altro ordine"
        buttonNuovoOrdine.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
