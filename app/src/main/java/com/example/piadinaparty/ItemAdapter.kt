package com.example.piadinaparty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.model.Item

class ItemAdapter(private val items: List<Item>, private val onItemClicked: () -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        val quantityTextView: TextView = itemView.findViewById(R.id.item_quantity)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        val addButton: Button = itemView.findViewById(R.id.add_button)
        val removeButton: Button = itemView.findViewById(R.id.remove_button)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}