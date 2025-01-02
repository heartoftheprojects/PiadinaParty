package com.example.piadinaparty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.model.Offerta

class OfferteAdapter(
    private val offers: List<Offerta>,
    private val onOfferClick: (Offerta) -> Unit
) : RecyclerView.Adapter<OfferteAdapter.OfferViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offerte, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offers[position]
        holder.bind(offer, position == selectedPosition) {
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            // Notifica le modifiche per aggiornare la visualizzazione degli elementi selezionati e deselezionati
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)

            onOfferClick(offer)
        }
    }

    override fun getItemCount(): Int = offers.size

    fun getSelectedOffer(): Offerta? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            offers[selectedPosition]
        } else {
            null
        }
    }

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.offerDescription)
        private val pointsRequiredTextView: TextView = itemView.findViewById(R.id.pointsRequired)
        private val offerPriceTextView: TextView = itemView.findViewById(R.id.offerPrice)

        fun bind(offer: Offerta, isSelected: Boolean, onClick: () -> Unit) {
            descriptionTextView.text = offer.description
            pointsRequiredTextView.text = "Punti richiesti: ${offer.pointsRequired}"
            offerPriceTextView.text = "Prezzo: €${offer.price}"

            itemView.setBackgroundColor(
                if (isSelected) itemView.context.getColor(R.color.selected_item)
                else itemView.context.getColor(R.color.default_item)
            )

            itemView.setOnClickListener {
                onClick()
            }
        }
    }
}