package com.example.piadinaparty.model

import android.os.Parcel
import android.os.Parcelable

data class Offerta(
    val id: String,
    val description: String,
    val price: Double,
    val pointsRequired: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(pointsRequired)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Offerta> {
        override fun createFromParcel(parcel: Parcel): Offerta {
            return Offerta(parcel)
        }

        override fun newArray(size: Int): Array<Offerta?> {
            return arrayOfNulls(size)
        }
    }
}