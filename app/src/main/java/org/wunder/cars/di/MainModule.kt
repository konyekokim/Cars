package org.wunder.cars.di

import dagger.Module
import dagger.Provides
import org.wunder.cars.data.ApiService
import org.wunder.cars.persistence.RealmService
import org.wunder.cars.views.mvp.MainContract
import org.wunder.cars.views.mvp.MainPresenter

@Module
class MainModule internal constructor(internal var view: MainContract.View) {

    @Provides
    @MainScope
    fun providesMainView(): MainContract.View {
        return view
    }

    @Provides
    @MainScope
    fun providesMainPresenter(view: MainContract.View, apiService: ApiService,
                              realmService: RealmService): MainPresenter {
        return MainPresenter(view, apiService, realmService)
    }
}