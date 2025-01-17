package com.example.piadinaparty.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.model.Item
import com.example.piadinaparty.view.adapter.ItemAdapter
import com.example.piadinaparty.R
import com.example.piadinaparty.model.Offerta

class FragmentHome : Fragment() {

    private lateinit var piadineAdapter: ItemAdapter //adapter di tipo ItemAdapter per visualizzare recyclerview di piadine
    private lateinit var bevandeAdapter: ItemAdapter
    private val piadineList = mutableListOf<Item>() //lista vuota e modificabile di tipo Item
    private val bevandeList = mutableListOf<Item>()
    private lateinit var totalOrderTextView: TextView //totale ordine visibile nella home di tipo TextView
    private var selectedOffer: Offerta? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //popoliamo gli adapter con gli Item delle due liste (piadineList e bevandeList)
        //L'utente interagisce con un elemento della lista (seleziona o deseleziona una piadina) , L'adapter rileva l'interazione e chiama la funzione updateOrder() che Calcola il totale corrente dell'ordine e aggiorna la TextView con il valore aggiornato.
        piadineAdapter = ItemAdapter(piadineList) { updateOrder() }
        bevandeAdapter = ItemAdapter(bevandeList) { updateOrder() }

        //Imposta il LayoutManager e l'adapter per le RecyclerView
        view.findViewById<RecyclerView>(R.id.recyclerPiadine).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = piadineAdapter
        }

        view.findViewById<RecyclerView>(R.id.recyclerBibite).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bevandeAdapter
        }

        totalOrderTextView = view.findViewById(R.id.totalOrderTextView)

        // Recupera l'offerta selezionata dal Bundle
        selectedOffer = arguments?.getParcelable("selectedOffer")

        // Popola le liste con dati di esempio
        populateLists()

        // Imposta il listener per il pulsante di conferma
        view.findViewById<Button>(R.id.ConfermaBottom).setOnClickListener {
            val offerPrice = selectedOffer?.price ?: 0.0
            val offerPoints = selectedOffer?.pointsRequired ?: 0
            val total = calculateTotalOrder(offerPrice)
            if (total > 0) { //se il totale dell'ordine è >0
                val selectedItems = ArrayList<Item>() //selectedItems è un arrayList di tipo Item che prende tutti gli item con quantità >0  sia della lista di piadine e sia della lista di bevande
                selectedItems.addAll(piadineList.filter { it.quantity > 0 })
                selectedItems.addAll(bevandeList.filter { it.quantity > 0 })

                val bundle = Bundle().apply { //serve per passare gli item selezionati e l'offerta selezionata all'activity di inserimento dati
                    putParcelableArrayList("selectedItems", selectedItems)
                    putParcelable("selectedOffer", selectedOffer)
                }

                val intent = Intent(activity, ActivityInserimentoDatiOrdine::class.java).apply {
                    putExtras(bundle)
                    putExtra("totalOrder", total)
                    putExtra("offerPoints", offerPoints)
                }
                Log.d("FragmentHome", "Selected items: $selectedItems, Total: $total")
                startActivity(intent)
            } else {
                Toast.makeText(activity, "Seleziona almeno una piadina o una bevanda per procedere", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    //funzione per popolare le due liste presenti nel fragment Home
    private fun populateLists() {
        piadineList.add(Item("Niente", 5.0, description = "salsiccia, patatine e doppia salsa"))
        piadineList.add(Item("Salame e formaggio", 4.5, description = "Salame e formaggio fresco"))
        bevandeList.add(Item("Coca Cola", 2.0, description = "Bibita gassata"))
        bevandeList.add(Item("Acqua", 1.0, description = "Acqua naturale"))
        piadineAdapter.notifyDataSetChanged() //notifica adapter degli item aggiunti alla lista di piadine
        bevandeAdapter.notifyDataSetChanged()
    }

    private fun updateOrder() {
        val offerPrice = selectedOffer?.price ?: 0.0
        val total = calculateTotalOrder(offerPrice)
        totalOrderTextView.text = "Totale: €%.2f".format(total) //La TextView viene aggiornata dinamicamente per riflettere il totale corrente dell'ordine.
    }

    //funzione per calcolare il totale dell'ordine
    private fun calculateTotalOrder(offerPrice: Double = 0.0): Double {
        var total = offerPrice
        for (item in piadineList) {
            total += item.price * item.quantity
        }
        for (item in bevandeList) {
            total += item.price * item.quantity
        }
        return total
    }
}