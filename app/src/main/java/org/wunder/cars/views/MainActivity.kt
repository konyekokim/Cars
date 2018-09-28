package org.wunder.cars.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.wunder.cars.Cars
import org.wunder.cars.R
import org.wunder.cars.di.MainModule
import org.wunder.cars.views.fragments.MainFragment
import org.wunder.cars.views.fragments.MapFragment
import org.wunder.cars.views.mvp.MainContract
import org.wunder.cars.views.mvp.MainPresenter
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View,
        MainFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener {

    override fun onPlacementSelected(placements: List<Placement>, pos: Int) {
        showMap(placements, pos)
    }

    override fun onGetPlacements(placements: List<Placement>) {
        this.placements = placements
        showMain(placements)
    }

    override fun onGetPlacementError(error: String) {
        if (error.contains("host") || error.contains("failed to connect")
                || error.contains("Timeout"))
            showNetworkError()
        else {
            snackBar = Snackbar.make(fragment_container, "An Error Occurred. $error",
                    Snackbar.LENGTH_LONG)
            snackBar?.show()
        }

    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun showComplete() {

    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    private var snackBar: Snackbar? = null
    private var fragment: Fragment = Fragment()
    private var mainFragment: MainFragment? = null
    var placements: List<Placement> = ArrayList()

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as Cars)
                .getAppComponent()
                .add(MainModule(this))
                .inject(this)

        /*when {
            isConnected -> presenter.getPlacements()
            presenter.getPlacementOffline().size > 0 -> {
                placements = presenter.getPlacementOffline()
                showMain(presenter.getPlacementOffline())
            }
            else -> showNetworkError()
        }*/

        hasInternetConnection().subscribe({
            if (it) {
                presenter.getPlacements()
            } else {
                val placements = presenter.getPlacementOffline()
                if (placements != null && placements.isNotEmpty())
                    showMain(placements)
                else
                    showNetworkError()
            }
            Timber.i("We got there and data was returned")
        }, {
            Timber.e(it, "We got there")
            showNetworkError()
            val placements = presenter.getPlacementOffline()
            if (placements != null && placements.isNotEmpty())
                showMain(placements)
        })
    }

    override fun onBackPressed() {
        if (mainFragment != null && mainFragment!!.isAdded)
            super.onBackPressed()
        else
            showMain(placements)

    }

    private fun showNetworkError() {
        snackBar = Snackbar.make(fragment_container, "No Internet Connection! Please Check and",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("TRY AGAIN") {
                    presenter.getPlacements()
                    snackBar?.dismiss()
                }
        snackBar?.show()
    }

    private fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                Timber.e(e)
                false
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadFragment(fragment: Fragment) {
        this.fragment = fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, "Cars")
                .commitAllowingStateLoss()
    }

    private fun showMain(placements: List<Placement>) {
        if (mainFragment == null) {
            mainFragment = MainFragment()
            val args = Bundle()
            args.putParcelableArrayList("placements", ArrayList(placements))
            mainFragment!!.arguments = args
        }
        fragment = mainFragment!!
        loadFragment(mainFragment!!)
    }

    private fun showMap(placements: List<Placement>, pos: Int) {
        val fragment = MapFragment()
        val args = Bundle()
        args.putParcelableArrayList("placements", ArrayList(placements))
        args.putInt("current_index", pos)
        fragment.arguments = args
        loadFragment(fragment)
    }
}
