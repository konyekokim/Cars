package org.wunder.cars.di

import dagger.Component
import org.wunder.cars.data.SchedulerProviderModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SchedulerProviderModule::class])
interface AppComponent {

    fun add(module: MainModule) : MainComponent
}
