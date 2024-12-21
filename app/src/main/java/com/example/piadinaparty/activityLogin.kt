package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class activityLogin : AppCompatActivity(){
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            val btnAccedi = findViewById<Button>(R.id.Accedi)

            btnAccedi.setOnClickListener {
                val intent = Intent(this, activityHome::class.java)
                startActivity(intent)}

            val btnRegistrati = findViewById<Button>(R.id.Registrati)

            btnRegistrati.setOnClickListener {
                val intent = Intent(this, activityRegistrazione::class.java)
                startActivity(intent)
            }
        }
}