package com.example.piadinaparty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.R
import com.example.piadinaparty.model.Ordine

class OrdineAdapter : ListAdapter<Ordine, OrdineAdapter.OrdineViewHolder>(OrdineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ordine, parent, false)
        return OrdineViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdineViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    class OrdineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemsTextView: TextView = itemView.findViewById(R.id.itemsTextView)
        private val frequencyTextView: TextView = itemView.findViewById(R.id.frequencyTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

        fun bind(order: Ordine) {
            itemsTextView.text = order.items.joinToString { it.name }
            frequencyTextView.text = order.frequency.toString()
            priceTextView.text = "Prezzo: €${order.prezzo}"
        }
    }

    class OrdineDiffCallback : DiffUtil.ItemCallback<Ordine>() {
        override fun areItemsTheSame(oldItem: Ordine, newItem: Ordine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ordine, newItem: Ordine): Boolean {
            return oldItem == newItem
        }
    }
}