package com.example.piadinaparty.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.R
import com.example.piadinaparty.model.Item

//classe ItemAdapater ha come parametro una lista di tipo Item
class ItemAdapter(private val items: List<Item>, private val onItemClicked: () -> Unit) : //onItemClicked eseguita ogni volta che vengono premuti i button ed utile per aggiornare la UI
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {   //la classe è una sottoclasse di RecyclerView.Adapter con un viewHolder personalizzato chiamato ItemVIewHolder

    //classe interna che rappresenta la vista dei singoli Item
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        val quantityTextView: TextView = itemView.findViewById(R.id.item_quantity)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        val addButton: Button = itemView.findViewById(R.id.add_button)
        val removeButton: Button = itemView.findViewById(R.id.remove_button)

        //setta i valori dei campi
        fun bind(item: Item) {
            nameTextView.text = item.name
            priceTextView.text = "€%.2f".format(item.price)
            quantityTextView.text = item.quantity.toString()
            descriptionTextView.text = item.description
            addButton.setOnClickListener {
                item.quantity++
                quantityTextView.text = item.quantity.toString()
                onItemClicked()
            }
            removeButton.setOnClickListener {
                if (item.quantity > 0) {
                    item.quantity--
                    quantityTextView.text = item.quantity.toString()
                    onItemClicked()
                }
            }
        }
    }

    //Crea il layout di un singolo item e restituisce un'istanza di ItemViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    //Associa i dati di un oggetto Item specifico (alla posizione position) al ViewHolder usando il metodo bind
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    //restituisce il numero di Item
    override fun getItemCount(): Int = items.size
}