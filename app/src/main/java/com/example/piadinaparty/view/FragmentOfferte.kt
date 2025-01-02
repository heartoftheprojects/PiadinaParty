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
import com.example.piadinaparty.OfferteAdapter
import com.example.piadinaparty.R

class FragmentOfferte : Fragment() {

    private lateinit var offersAdapter: OfferteAdapter
    private val offersList = mutableListOf<Offerta>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offerte, container, false)

        // Inizializza l'adapter con la lista delle offerte e il listener per il click
        offersAdapter = OfferteAdapter(offersList) { offer ->
            // Gestisci il click sull'offerta
            Toast.makeText(activity, "Offerta selezionata: ${offer.description}", Toast.LENGTH_SHORT).show()
        }

        // Imposta il LayoutManager e l'adapter per la RecyclerView
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
                val fragmentHome = FragmentHome().apply {
                    arguments = Bundle().apply {
                        putDouble("offerPrice", selectedOffer.price)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragmentHome)
                    .commit()
            } else {
                Toast.makeText(activity, "Seleziona un'offerta per procedere", Toast.LENGTH_SHORT).show()
            }
        }

        // Popola la lista delle offerte
        populateOffersList()

        return view
    }

    private fun populateOffersList() {
        // Aggiungi le offerte alla lista
        offersList.add(Offerta("1", "Niente + Coca Cola", 5.0, 100))
        offersList.add(Offerta("2", "Crudo + acqua", 4.5, 50))
        offersAdapter.notifyDataSetChanged()
    }
}


//cose da fare in questo fragment:
//gli elementi della lista di offerte devo renderli selezionabili
//ed inserire un button conferma che cliccandolo dopo aver selezionato un offerta, venga effettuato il controllo che l'utente abbia abbastanza punti per sbloccare l'offerta e in caso positivo mi reindirizzo alla pagina di inserimento dati ordine

//cose da fare in generale:
//capire bene come sviluppare il meccanismo di punti accumulati dall'utente