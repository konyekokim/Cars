package org.wunder.cars.views.mvp

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.wunder.cars.data.ApiService
import org.wunder.cars.persistence.RealmService
import org.wunder.cars.views.Placement
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainPresenter @Inject
internal constructor(view: MainContract.View, private val apiService: ApiService,
                     private val realmService: RealmService) : MainContract.Presenter {
    override fun getPlacementOffline(): List<Placement>? {
        return realmService.getAllPlacements()
    }

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onDetach() {
        compositeDisposable.dispose()
        realmService.closeRealm()
    }

    private val view: WeakReference<MainContract.View>?

    init {
        this.view = WeakReference(view)
    }

    override fun getPlacements() {
        view?.get()?.showLoading()
        if (realmService.getAllPlacements() == null) {
            placements()
        } else {
            if (realmService.getAllPlacements()!!.isNotEmpty()) {
                view?.get()?.onGetPlacements(realmService.getAllPlacements()!!)
                view?.get()?.hideLoading()
            } else {
                placements()
            }
        }
    }

    private fun placements() {
        val disposable = apiService.placements.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    Timber.i(t.placemarks.toString())
                    view?.get()?.onGetPlacements(t.placemarks)
                    view?.get()?.hideLoading()
                    t.placemarks.forEach {
                        realmService.addPlacement(it)
                    }
                }, {
                    Timber.e(it, it.message)
                    view?.get()?.hideLoading()
                    view?.get()?.onGetPlacementError(it.message!!)
                })
        compositeDisposable.add(disposable)
    }
}