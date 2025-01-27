package com.example.piadinaparty.model

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val name: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val description: String = ""
) : Parcelable {

    //Serve per ricostruire l'oggetto Item dai dati che sono stati "impacchettati" in un Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    //Serve per scrivere i dati di un oggetto Item in un Parcel e prepara l'oggetto per essere trasferito tra componenti
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
        parcel.writeString(description)
    }

    //Indica il tipo di contenuti nel Parcel (di solito ritorna 0)
    override fun describeContents(): Int {
        return 0
    }

    //È un oggetto necessario per dire ad Android come creare un oggetto Item da un Parcel
    companion object CREATOR : Parcelable.Creator<Item> {

        //Crea un nuovo oggetto Item usando il costruttore che accetta un Parcel
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        //Crea un array di oggetti Item (utilizzato quando si trasferiscono più oggetti come nel nostro caso l'arraylist di item selezionati nel fragmentHome)
        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}