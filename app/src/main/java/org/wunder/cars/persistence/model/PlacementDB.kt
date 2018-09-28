package org.wunder.cars.persistence.model

import io.realm.RealmList
import io.realm.RealmObject

open class PlacementDB(var address: String? = "",
                       var coordinates: RealmList<Float>? = null,
                       var engineType: String? = "",
                       var exterior: String? = "",
                       var fuel: Int? = 0,
                       var interior: String? = "",
                       var name: String? = "",
                       var vin: String? = "") : RealmObject()