package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schermata1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.schermata1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //pulsante registrazione
        val btnRegistrati = findViewById<Button>(R.id.Registrati)

        btnRegistrati.setOnClickListener {
            val intent = Intent(this, activity_schermata1_1::class.java)
            startActivity(intent)
        }
    }
}