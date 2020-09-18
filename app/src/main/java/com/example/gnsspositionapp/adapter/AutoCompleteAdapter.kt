package com.example.gnsspositionapp.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.WorkerThread
import java.util.regex.Pattern
import javax.inject.Singleton
import kotlin.math.min

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
class BKTree {

    private lateinit var tree : Node

    class Node(val value : String , val children : MutableMap<Int,Node>)

    @WorkerThread
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

    @WorkerThread
    fun search(radius : Int,word : String) : List<String> {
        val candidate = ArrayDeque<Node>()
        candidate.add(tree)
        val result = arrayListOf<String>()

        while (candidate.isNotEmpty()){
            val current = candidate.removeFirst()

            val dist = dist(word,current.value)

            if(dist <= radius){
                result.add(current.value)
            }

            val low = dist-radius
            val high = dist+radius

            current.children.filter {
                it.key in low..high
            }.forEach { candidate.addLast(it.value) }
            
        }

        return result
    }

    fun dist(s : String , t : String) : Int{
        val sLen = s.length + 1
        val tLen = t.length + 1

        var cost = Array(sLen){ it }

        var newCost = Array(sLen){ 0 }

        for (i in 1 until tLen){
            newCost[0] = i

            for( j in 1 until sLen){
                val match = if(s[j-1] == t[i-1]) 0 else 1

                val replaceCost = cost[j-1] + match
                val insertCost = cost[j] + 1
                val deleteCost = newCost[j-1] + 1

                newCost[j] = min(min(replaceCost,insertCost),deleteCost)
            }

            val temp = cost

            cost = newCost

            newCost = temp
        }

        return cost[sLen-1]
    }
}