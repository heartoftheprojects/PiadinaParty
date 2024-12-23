package com.example.piadinaparty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val data: List<String>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Lista per memorizzare gli elementi selezionati
    private val selectedItems = mutableSetOf<String>()

    class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tvNomeProdotto)
        val textView1: TextView = view.findViewById(R.id.tvPrezzoProdotto)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_prodotto, parent, false)
        return ProductViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item

        // Aggiorna il CheckBox in base alla selezione
        holder.checkBox.isChecked = selectedItems.contains(item)

        // Gestione del click sul CheckBox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(item)
            } else {
                selectedItems.remove(item)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    // Metodo per ottenere gli elementi selezionati
    fun getSelectedItems(): List<String> = selectedItems.toList()
}
