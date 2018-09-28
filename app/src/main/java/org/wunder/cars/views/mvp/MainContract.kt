package org.wunder.cars.views.mvp

import org.wunder.cars.views.Placement

interface MainContract {

    interface View {
        fun onGetPlacements(placements: List<Placement>)
        fun onGetPlacementError(error: String)
        fun showLoading()
        fun showComplete()
        fun hideLoading()
    }

    interface Presenter {
        fun getPlacements()
        fun getPlacementOffline(): List<Placement>?
        fun onDetach()
    }
}