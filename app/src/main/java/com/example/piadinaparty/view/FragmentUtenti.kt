package com.example.piadinaparty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.piadinaparty.R
import com.example.piadinaparty.controller.UtenteController
import com.example.piadinaparty.databinding.FragmentUtentiBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.AlertDialog
import android.content.Intent

class FragmentUtenti : Fragment() {

    private lateinit var binding: FragmentUtentiBinding
    private lateinit var userController: UtenteController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUtentiBinding.inflate(inflater, container, false)
        userController = UtenteController(requireContext())

        fetchUserData()
        setupBackButton()
        setupLogoutButton()

        return binding.root
    }

    private fun setupLogoutButton() {   //funzionamento del button per il logout
        binding.buttonLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Sicuro di voler uscire dall'applicazione?")
                .setPositiveButton("Si") { _, _ ->
                    userController.logoutUser()
                    val intent = Intent(requireContext(), ActivityLogin::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    //funzione per visualizzare i campi dell'utente recuperati da firebase mediante la funzione fetchUserData
    private fun fetchUserData() {
        userController.fetchUserData { user ->
            if (user != null) {
                binding.textView7.text = "Nome: ${user.firstName}"
                binding.textView8.text = "Cognome: ${user.lastName}"
                binding.textView9.text = "Email: ${user.email}"
                binding.textViewPoints.text = "Punti: ${user.points}"
            } else {
                binding.textView7.text = "Nome: N/A"
                binding.textView8.text = "Cognome: N/A"
                binding.textView9.text = "Email: N/A"
                binding.textViewPoints.text = "Punti: N/A"
            }
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView?.selectedItemId = R.id.bottom_home
        }
    }
}