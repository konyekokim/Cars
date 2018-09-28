package org.wunder.cars.data

import io.reactivex.Observable
import org.wunder.cars.views.Placements
import retrofit2.http.GET

interface ApiService {
    companion object {
        val BASE_URL = "https://s3-us-west-2.amazonaws.com/wunderbucket/"
    }

    @get:GET("locations.json") val placements: Observable<Placements>
}