package com.example.piadinaparty.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piadinaparty.R
import com.example.piadinaparty.model.Offerta

class OfferteAdapter(
    private val offers: List<Offerta>,
    private val onOfferClick: (Offerta) -> Unit
) : RecyclerView.Adapter<OfferteAdapter.OfferViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION //variabile che tiene conto della posizione dell'offerta selezionata

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offerte, parent, false)
        return OfferViewHolder(view)
    }

    //Holder è un'istanza del ViewHolder che rappresenta la vista da aggiornare
    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offers[position] //Si recupera l'offerta dalla lista offers usando la posizione corrente
        holder.bind(offer, position == selectedPosition) {  //colleghiamo i dati dell'offerta alla vista e position == selectedPosition verifica se l'elemento in questa posizione è quello selezionato.
            val previousSelectedPosition = selectedPosition //salva la posizione dell'elemento precedentemente selezionato
            selectedPosition = holder.adapterPosition //selectedPosition indica che il nuovo elemento selezionato è quello cliccato.

            //Informiamo il RecyclerView che la UI di due elementi è cambiata
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)

            //informai che l'offerta è stata cliccata e quindi possono avvenire azioni sull'offerta cliccata
            onOfferClick(offer)
        }
    }

    override fun getItemCount(): Int = offers.size

    //funzione che controlla che sia stata selezionata un'offerta e restituisce in caso l'offerta che si trova nella posizione selezionata
    fun getSelectedOffer(): Offerta? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            offers[selectedPosition]
        } else {
            null
        }
    }

    //classe per settare i campi delll'item relativo all'offerta
    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.offerDescription)
        private val pointsRequiredTextView: TextView = itemView.findViewById(R.id.pointsRequired)
        private val offerPriceTextView: TextView = itemView.findViewById(R.id.offerPrice)

        fun bind(offer: Offerta, isSelected: Boolean, onClick: () -> Unit) {
            descriptionTextView.text = offer.description
            pointsRequiredTextView.text = "Punti richiesti: ${offer.pointsRequired}"
            offerPriceTextView.text = "Prezzo: €${offer.price}"

            //setti lo sfondo della view dell'item relativo alll'offerta con il colore blu se l'utente seleziona un'offerta
            itemView.setBackgroundColor(
                if (isSelected) itemView.context.getColor(R.color.selected_item)
                else itemView.context.getColor(R.color.default_item)
            )

            //setti il listener per consentire all'utente di cliccare e selezionare un'offerta
            itemView.setOnClickListener {
                onClick()
            }
        }
    }
}