package org.wunder.cars.di

import dagger.Subcomponent
import org.wunder.cars.views.MainActivity

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)
}