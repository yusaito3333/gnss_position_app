package com.example.gnsspositionapp.ui.measure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.data.LocationInfo
import com.example.gnsspositionapp.databinding.MeasurementListItemBinding

class LocationListAdapter(private var isYard : Boolean) : ListAdapter<LocationInfo,ViewHolder>(ItemCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.measurement_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),isYard)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position)
        }else{
            holder.convertUnit(isYard)
        }
    }

    fun convertUnit(isYard: Boolean) {
        this.isYard = isYard
        notifyItemRangeChanged(0,itemCount,Unit)
    }
}

class ViewHolder(private val binding : MeasurementListItemBinding)
    : RecyclerView.ViewHolder(binding.root){
    fun bind(location : LocationInfo,isYard: Boolean) {
        binding.location = location
        binding.isYard = isYard
        binding.executePendingBindings()
    }

    fun convertUnit(isYard: Boolean) {
        binding.isYard = isYard
        binding.executePendingBindings()
    }
}

object ItemCallBack : DiffUtil.ItemCallback<LocationInfo>() {
    override fun areItemsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
        return oldItem.date.isEqual(newItem.date)
    }

    override fun areContentsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
        return oldItem == newItem
    }
}