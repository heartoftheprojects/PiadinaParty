package com.example.piadinaparty

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class fragmentHome : Fragment() {

    private lateinit var piadineAdapter: ItemAdapter
    private lateinit var bevandeAdapter: ItemAdapter
    private val piadineList = mutableListOf<Item>()
    private val bevandeList = mutableListOf<Item>()
    private lateinit var totalOrderTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        piadineAdapter = ItemAdapter(piadineList) { updateOrder() }
        bevandeAdapter = ItemAdapter(bevandeList) { updateOrder() }

        // Imposta il LayoutManager e l'adapter per le RecyclerView
        view.findViewById<RecyclerView>(R.id.recyclerPiadine).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = piadineAdapter
        }

        view.findViewById<RecyclerView>(R.id.recyclerBibite).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bevandeAdapter
        }

        totalOrderTextView = view.findViewById(R.id.totalOrderTextView)

        // Popola le liste con dati di esempio
        populateLists()

        // Imposta il listener per il pulsante di conferma
        view.findViewById<Button>(R.id.ConfermaBottom).setOnClickListener {
            val intent = Intent(activity, activityInserimentoDatiOrdine::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun populateLists() {
        piadineList.add(Item("Niente", 5.0, description = "salsiccia, patatine e doppia salsa"))
        piadineList.add(Item("Salame e formaggio", 4.5, description = "Salame e formaggio fresco"))
        bevandeList.add(Item("Coca Cola", 2.0, description = "Bibita gassata"))
        bevandeList.add(Item("Acqua", 1.0, description = "Acqua naturale"))
        piadineAdapter.notifyDataSetChanged()
        bevandeAdapter.notifyDataSetChanged()
    }

    private fun updateOrder() {
        var total = 0.0
        for (item in piadineList) {
            total += item.price * item.quantity
        }
        for (item in bevandeList) {
            total += item.price * item.quantity
        }
        totalOrderTextView.text = "Totale: €%.2f".format(total)
    }
}