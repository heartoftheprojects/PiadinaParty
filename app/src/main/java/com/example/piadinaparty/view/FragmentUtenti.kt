package com.example.piadinaparty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.piadinaparty.controller.UtenteController
import com.example.piadinaparty.databinding.FragmentUtentiBinding

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

        return binding.root
    }

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
}