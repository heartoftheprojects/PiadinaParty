package com.example.piadinaparty.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.R
import com.example.piadinaparty.controller.OrdineController
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewOrdiniFrequenti)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = orderAdapter

        val userId: String = getCurrentUserId()
        loadFrequentOrders(userId)

        view.findViewById<Button>(R.id.Indietro).setOnClickListener {
            // Torna al fragmentHome
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, FragmentHome())
                .commit()
        }

        return view
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("FragmentOrdiniFrequenti", "Current User ID: ${currentUser?.uid}")
        return currentUser?.uid ?: ""
    }

    private fun loadFrequentOrders(userId: String) {
        orderController.getFrequentOrders(userId) { orders ->
            if (orders.isNotEmpty()) {
                orders.forEach { order ->
                    Log.d("FragmentOrdiniFrequenti", "Order: ${order.id}, Items: ${order.items.joinToString { it.name }}")
                }
            } else {
                Log.d("FragmentOrdiniFrequenti", "No orders found")
            }
            orderAdapter.submitList(orders)
        }
    }
}