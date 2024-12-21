package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class activityRegistrazione : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione)

        val btnConferma = findViewById<Button>(R.id.Conferma)

        btnConferma.setOnClickListener {
            val intent = Intent(this, activityRegistrazioneSuccesso::class.java)
            startActivity(intent)
        }
    }
}
