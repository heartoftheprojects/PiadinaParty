package com.example.piadinaparty.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.R
import com.example.piadinaparty.controller.OrdineController
import com.example.piadinaparty.model.Ordine
import com.example.piadinaparty.OrdineAdapter
import com.google.firebase.auth.FirebaseAuth

class FragmentOrdiniFrequenti : Fragment() {
    private lateinit var orderController: OrdineController
    private lateinit var orderAdapter: OrdineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ordinifrequenti, container, false)
        orderController = OrdineController()
        orderAdapter = OrdineAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = orderAdapter

        val userId: String = getCurrentUserId()
        loadFrequentOrders(userId)

        return view
    }

    private fun getCurrentUserId(): String {
        // Ottieni l'ID utente corrente da Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }

    private fun loadFrequentOrders(userId: String) {
        orderController.getFrequentOrders(userId) { orders ->
            orderAdapter.submitList(orders)
        }
    }
}