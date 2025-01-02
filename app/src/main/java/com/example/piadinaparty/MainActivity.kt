package com.example.piadinaparty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.piadinaparty.view.FragmentHome
import com.example.piadinaparty.view.FragmentUtenti
import com.example.piadinaparty.view.FragmentOfferte
import com.example.piadinaparty.view.FragmentOrdiniFrequenti
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(FragmentHome())
                    true
                }
                R.id.bottom_utente -> {
                    replaceFragment(FragmentUtenti())
                    true
                }
                R.id.bottom_offerte -> {
                    replaceFragment(FragmentOfferte())
                    true
                }
                R.id.bottom_ordinifrequenti -> {
                    replaceFragment(FragmentOrdiniFrequenti())
                    true
                }
                else -> false
            }
        }
        val fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")
        if (fragmentToLoad == "HOME_FRAGMENT") {
            bottomNavigationView.selectedItemId = R.id.bottom_home
        } else {
            // Carica il HomeFragment come predefinito
            replaceFragment(FragmentHome())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}