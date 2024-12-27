package com.example.piadinaparty

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

//import com.example.piadinaparty.ProductAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId)
            {
                R.id.bottom_home ->
                {
                    replaceFragment(fragmentHome())
                    true
                }
                R.id.bottom_utente ->
                {
                    replaceFragment(fragmentUtenti())
                    true
                }
                R.id.bottom_offerte ->
                {
                    replaceFragment(fragmentOfferte())
                    true
                }
                R.id.bottom_ordinifrequenti ->
                {
                    replaceFragment(fragmentOrdiniFrequenti())
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
            replaceFragment(fragmentHome())
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}