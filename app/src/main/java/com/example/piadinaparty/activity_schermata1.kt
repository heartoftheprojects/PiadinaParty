package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class activity_schermata1 : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_schermata1)

            val btnRegistrati = findViewById<Button>(R.id.Registrati)

            btnRegistrati.setOnClickListener {
                val intent = Intent(this, activity_schermata1_1::class.java)
                startActivity(intent)
            }
        }
}