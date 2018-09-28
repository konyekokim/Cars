package org.wunder.cars.persistence

import org.wunder.cars.views.Placement

interface RealmInterface {

    fun getAllPlacements() : List<Placement>?

    fun addPlacement(placement: Placement) : Boolean

    fun getPlacement(name: String) : Placement?

    fun updatePlacement(placement: Placement) : Boolean

    fun closeRealm()
}