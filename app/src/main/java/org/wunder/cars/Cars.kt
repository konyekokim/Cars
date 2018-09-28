package org.wunder.cars

import android.app.Activity
import android.app.Application
import io.realm.Realm
import org.wunder.cars.di.AppComponent
import org.wunder.cars.di.AppModule
import org.wunder.cars.di.DaggerAppComponent
import timber.log.Timber

class Cars: Application() {

    internal lateinit var component: AppComponent

    operator fun get(activity: Activity): Cars {
        return activity.application as Cars
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        Realm.init(this)
        Timber.plant(Timber.DebugTree())
    }

    fun getAppComponent(): AppComponent {
        return component
    }
}