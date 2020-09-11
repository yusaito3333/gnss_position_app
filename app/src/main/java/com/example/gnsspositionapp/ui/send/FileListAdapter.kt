package com.example.gnsspositionapp.ui.send

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.databinding.FileListItemBinding
import java.io.File

class FileListAdapter : ListAdapter<File,FileListAdapter.ViewHolder>(ItemCallBack) {

    class ViewHolder(val binding : FileListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(file :File){
            binding.tvFileName.text = file.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.file_list_item,
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object ItemCallBack : DiffUtil.ItemCallback<File>(){
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }

}