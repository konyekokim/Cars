package org.wunder.cars.data

import dagger.Module
import dagger.Provides
import org.wunder.cars.util.BaseScheduler
import org.wunder.cars.util.SchedulerProvider
import javax.inject.Singleton

@Module
class SchedulerProviderModule {

    @Provides
    @Singleton
    fun provideScheduler(): BaseScheduler {
        return SchedulerProvider.getInstance()
    }

    @Provides
    @Singleton
    fun scheduler(): SchedulerProvider {
        return SchedulerProvider.getInstance()
    }
}