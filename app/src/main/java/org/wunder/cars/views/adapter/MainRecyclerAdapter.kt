package org.wunder.cars.views.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.wunder.cars.R
import org.wunder.cars.views.Placement
import org.wunder.cars.views.fragments.MainFragment.OnFragmentInteractionListener

class MainRecyclerAdapter(placements: ArrayList<Placement>, listener: OnFragmentInteractionListener) :
        RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    private var placements = ArrayList<Placement>()
    private var listener: OnFragmentInteractionListener? = null

    init {
        this.placements = placements
        this.listener = listener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
                .inflate(R.layout.placement_list_item, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(h: ViewHolder, p1: Int) {
        val placement = placements[p1]

        h.carAds.text = placement.address
        h.carEngType.text = placement.engineType
        h.carExterior.text = placement.exterior
        h.carFuel.text = placement.fuel.toString()
        h.carInterior.text = placement.interior
        h.carName.text = placement.name
        h.carVIN.text = placement.vin

        if (placement.interior != "GOOD")
            h.carInterior.setTextColor(ContextCompat.getColor(h.carInterior.context, R.color.red))
        else
            h.carInterior.setTextColor(ContextCompat.getColor(h.carInterior.context,
                    R.color.colorAccent2))

        if (placement.exterior != "GOOD")
            h.carExterior.setTextColor(ContextCompat.getColor(h.carInterior.context, R.color.red))
        else
            h.carExterior.setTextColor(ContextCompat.getColor(h.carInterior.context,
                    R.color.colorAccent2))

        if (placement.fuel!! < 40)
            h.carFuel.setTextColor(ContextCompat.getColor(h.carInterior.context, R.color.red))
        else if (placement.fuel!! > 50)
            h.carFuel.setTextColor(ContextCompat.getColor(h.carInterior.context,
                    R.color.colorAccent2))
        else
            h.carFuel.setTextColor(ContextCompat.getColor(h.carInterior.context,
                    R.color.black))

        h.itemView.setOnClickListener {
            if (listener != null)
                listener?.onPlacementSelected(placements, p1)
        }
    }

    override fun getItemCount(): Int {
        return placements.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val carName: TextView = v.findViewById(R.id.name)
        val carAds: TextView = v.findViewById(R.id.address)
        val carEngType: TextView = v.findViewById(R.id.engine)
        val carFuel: TextView = v.findViewById(R.id.fuel)
        val carVIN: TextView = v.findViewById(R.id.vin)
        val carInterior: TextView = v.findViewById(R.id.interior)
        val carExterior: TextView = v.findViewById(R.id.exterior)
    }
}