package org.wunder.cars.persistence

import io.realm.Realm
import io.realm.RealmList
import org.wunder.cars.views.Placement
import org.wunder.cars.persistence.model.PlacementDB
import timber.log.Timber

class RealmService(private val realm: Realm) : RealmInterface {
    override fun closeRealm() {
        realm.close()
    }

    override fun getAllPlacements() : List<Placement>? {
        try {
            val result = realm.where(PlacementDB::class.java).findAll()
            val placements = ArrayList<Placement>()
            if (result.size > 0)
                result.forEach {
                placements.add(it.toPlacement())
                }
            else
                return null
            return placements
        } catch (e: Exception) {
            Timber.e(e)
            return null
        }
    }

    override fun addPlacement(placement: Placement) : Boolean {
        val coordinates = RealmList<Float>()
        placement.coordinates?.forEach {
            coordinates.add(it)
        }
        val placementDB = PlacementDB(placement.address, coordinates,
                placement.engineType, placement.exterior, placement.fuel, placement.interior,
                placement.name, placement.vin)

        try {
            realm.run {
                beginTransaction()
                copyToRealm(placementDB)
                commitTransaction()
            }
            return true
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }
    }

    override fun getPlacement(name: String) : Placement? {
        try {
            val placementDB = realm.where(PlacementDB::class.java)
                    .contains("name", name)
                    .findFirst()
            return placementDB?.toPlacement()!!
        } catch (e: Exception) {
            Timber.e(e)
            return null
        }
    }

    override fun updatePlacement(placement: Placement) : Boolean{
        try {
            val result = realm.where(PlacementDB::class.java)
                    .contains("name", placement.name!!)
                    .findFirst()
            result?.address = placement.address
            val coordinates = RealmList<Float>()
            placement.coordinates?.forEach {
                coordinates.add(it)
            }
            result?.coordinates = coordinates
            result?.engineType = placement.engineType
            result?.exterior = placement.exterior
            result?.fuel = placement.fuel
            result?.interior = placement.interior
            result?.name = placement.name
            result?.vin = placement.vin
            result?.let {
                realm.run {
                    beginTransaction()
                    insertOrUpdate(it)
                    commitTransaction()
                }
            }
            return true
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }
    }


    private fun PlacementDB.toPlacement() : Placement {
        val placement = Placement()
        placement.address = this.address
        placement.coordinates = this.coordinates
        placement.engineType = this.engineType
        placement.exterior = this.exterior
        placement.fuel = this.fuel
        placement.interior = this.interior
        placement.name = this.name
        placement.vin = this.vin

        return placement
    }

}