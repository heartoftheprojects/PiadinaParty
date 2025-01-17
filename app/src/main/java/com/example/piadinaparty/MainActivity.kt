package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.piadinaparty.view.ActivityLogin
import com.example.piadinaparty.view.FragmentHome
import com.example.piadinaparty.view.FragmentUtenti
import com.example.piadinaparty.view.FragmentOfferte
import com.example.piadinaparty.view.FragmentOrdiniFrequenti
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) { //controllo per verificare se l'utente è loggato oppure no, in caso non è loggato allora l'applicazione apre activity di login
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
            return
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //nel bottomNavigationMenu a seconda del tasto cliccato viene aperto il rispettivo fragment
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    Log.d("MainActivity", "Navigazione a HomeFragment")
                    replaceFragment(FragmentHome())
                    true
                }
                R.id.bottom_utente -> {
                    Log.d("MainActivity", "Navigazione a FragmentUtenti")
                    replaceFragment(FragmentUtenti())
                    true
                }
                R.id.bottom_offerte -> {
                    Log.d("MainActivity", "Navigazione a FragmentOfferte")
                    replaceFragment(FragmentOfferte())
                    true
                }
                R.id.bottom_ordinifrequenti -> {
                    Log.d("MainActivity", "Navigazione a FragmentOrdiniFrequenti")
                    replaceFragment(FragmentOrdiniFrequenti())
                    true
                }
                else -> false
            }
        }

        //viene controllato che il fragment predefinito sia quello relativo alla home
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        if (fragmentToLoad == "HOME_FRAGMENT") {  //controllo che fragmentToLoad si riferisca ad un fragment in particolare (in questo caso Home)
            bottomNavigationView.selectedItemId = R.id.bottom_home
        } else {
            // se fragmentToLoad non ha una richiesta specifica allora carica il HomeFragment come predefinito
            replaceFragment(FragmentHome())
        }
    }

    private fun replaceFragment(fragment: Fragment) { //serve per caricare il fragment relativo all'item selezionato nel menu col clic sul bottom
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}