package com.example.gnsspositionapp.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import java.util.regex.Pattern
import javax.inject.Singleton

class AutoCompleteAdapter(context : Context, resource : Int,private val objects : List<String>)
    : ArrayAdapter<String>(context,resource,objects) {

    private lateinit var filter : CustomFilter

    override fun getFilter(): Filter {
        if(!::filter.isInitialized){
            filter = CustomFilter()
        }

        return filter
    }

    inner class CustomFilter : Filter(){

        private val pattern = Pattern.compile("c([bkcrfsl23]).*")

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val result = FilterResults()

            if(constraint != null){
                val m = pattern.matcher(constraint.toString())

                val list = arrayListOf<String>()
                if(m.find()){
                    val match = constraint.subSequence(m.start(),m.end()).toString()
                    for (s in objects){
                        if(s.startsWith(match)){
                            list.add(s)
                        }
                    }
                }

                result.count = list.size
                result.values = list
            }

            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }

    }
}

@Singleton
private class BKTree(dict : List<String>) {

    private lateinit var tree : Node

    class Node(val value : String , val children : MutableMap<Int,Node>)

    init {

    }

    fun add(node : Node){
        if(!::tree.isInitialized){
            tree = node
            return
        }else{
            var current = tree

            while(true){
                val dist = dist(node.value,current.value)

                if(current.children.containsKey(dist)){
                    current = current.children[dist]!!
                }else{
                    current.children[dist] = node
                    break
                }
            }
        }
    }

    private fun search(radius : Int,word : String) : List<String> {
        val candidate = ArrayDeque<Node>()
        candidate.add(tree)
        val result = arrayListOf<String>()

        while (true){
            val current = candidate.removeFirst()

            
        }
    }

    private fun dist(s : String , t : String) : Int{
        TODO()
    }
}