package org.wunder.cars.views

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Placement(var address: String? = "",
               var coordinates: List<Float>? = null,
               var engineType: String? = "",
               var exterior: String? = "",
               var fuel: Int? = 0,
               var interior: String? = "",
               var name: String? = "",
               var vin: String? = "") : Parcelable

data class Placements(val placemarks: List<Placement>)