package org.wunder.cars.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider: BaseScheduler{


    companion object {
        private var INSTANCE: SchedulerProvider? = null

        @Synchronized
        private fun createInstance() {
            if (INSTANCE == null) {
                INSTANCE = SchedulerProvider()
            }
        }

        fun getInstance(): SchedulerProvider {
            if (INSTANCE == null) createInstance()
            return INSTANCE!!
        }
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
