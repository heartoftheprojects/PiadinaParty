package com.example.piadinaparty.model

import android.os.Parcel
import android.os.Parcelable

data class Offerta(
    val id: String,
    val description: String,
    val price: Double,
    val pointsRequired: Int
) : Parcelable {

    //Serve per ricostruire l'oggetto Offerta dai dati che sono stati "impacchettati" in un Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    //Serve per scrivere i dati di un oggetto Offerta in un Parcel e prepara l'oggetto per essere trasferito tra componenti
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(pointsRequired)
    }

    //Indica il tipo di contenuti nel Parcel (di solito ritorna 0)
    override fun describeContents(): Int {
        return 0
    }

    //È un oggetto necessario per dire ad Android come creare un oggetto Item da un Parcel
    companion object CREATOR : Parcelable.Creator<Offerta> {

        //Crea un nuovo oggetto Offerta usando il costruttore che accetta un Parcel
        override fun createFromParcel(parcel: Parcel): Offerta {
            return Offerta(parcel)
        }
        //Crea un array di oggetti Offerta (utilizzato quando si trasferiscono più oggetti)
        override fun newArray(size: Int): Array<Offerta?> {
            return arrayOfNulls(size)
        }
    }
}