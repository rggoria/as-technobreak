package com.example.finalproject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
//data of note
class Notes(val title: String, val content: String): Parcelable{
    constructor(): this("","")
}