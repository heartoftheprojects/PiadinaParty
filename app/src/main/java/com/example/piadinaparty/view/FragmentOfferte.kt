package com.example.piadinaparty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.model.Offerta
import com.example.piadinaparty.view.adapter.OfferteAdapter
import com.example.piadinaparty.R
import com.example.piadinaparty.controller.UtenteController
import com.google.firebase.auth.FirebaseAuth

class FragmentOfferte : Fragment() {

    private lateinit var offersAdapter: OfferteAdapter //adapter per visualizzare la recyclerview di offerte
    private val offersList = mutableListOf<Offerta>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offerte, container, false)

        // Inizializza l'adapter con la lista delle offerte
        offersAdapter = OfferteAdapter(offersList) { offer ->
            Toast.makeText(activity, "Offerta selezionata: ${offer.description}", Toast.LENGTH_SHORT).show()
        }

        // Nella RecyclerView identificata da R.id.recyclerOffers si imposta il LayoutManager e l'adapter per la RecyclerView
        view.findViewById<RecyclerView>(R.id.recyclerOffers).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = offersAdapter
        }

        // Gestisci il click del pulsante "Indietro"
        view.findViewById<Button>(R.id.Indietro).setOnClickListener {
            // Torna al fragmentHome
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, FragmentHome())
                .commit()
        }

        // Gestisci il click del pulsante "Utilizza Offerta"
        view.findViewById<Button>(R.id.utilizzaOfferta).setOnClickListener {
            val selectedOffer = offersAdapter.getSelectedOffer()
            if (selectedOffer != null) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userController = UtenteController(requireContext())
                    userController.getUserPoints(userId) { points ->
                        if (points != null && points >= selectedOffer.pointsRequired) {
                            // Aggiorna i punti dell'utente
                            val newPoints = points - selectedOffer.pointsRequired
                            userController.updateUserPoints(userId, newPoints) { success ->
                                if (success) {
                                    val fragmentHome = FragmentHome().apply { //viene passata l'offerta selezionata al fragment home
                                        arguments = Bundle().apply {
                                            putParcelable("selectedOffer", selectedOffer)
                                        }
                                    }
                                    parentFragmentManager.beginTransaction() //ritorno al fragmentHome
                                        .replace(R.id.frame_container, fragmentHome)
                                        .commit()
                                } else {
                                    Toast.makeText(activity, "Errore nell'aggiornamento dei punti", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(activity, "Punti insufficienti per sbloccare l'offerta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(activity, "Seleziona un'offerta per procedere", Toast.LENGTH_SHORT).show()
            }
        }

        // Popola la lista delle offerte
        populateOffersList()

        return view
    }

    // Aggiungi le offerte alla lista
    private fun populateOffersList() {
        offersList.add(Offerta("1", "Niente + Coca Cola", 5.0, 8))
        offersList.add(Offerta("2", "Crudo + acqua", 4.5, 4))
        offersAdapter.notifyDataSetChanged()
    }
}