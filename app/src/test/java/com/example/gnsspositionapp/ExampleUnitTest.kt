package com.example.gnsspositionapp

import com.example.gnsspositionapp.adapter.BKTree
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val data = listOf("some","soft","same","mole","soda","salmon")

    @Test
    fun addition_isCorrect() = runBlocking {

        val tree = BKTree()

        val job = launch(Dispatchers.Default) {
            data.forEach { tree.add(BKTree.Node(it, mutableMapOf())) }
        }

        job.join()

        val searchList = tree.search(2,"sort")

        println(searchList)
        assert(searchList.contains("soft"))
        assert(searchList.contains("some"))
        assert(searchList.contains("soda"))
        assert(!searchList.contains("same"))
        assert(!searchList.contains("mole"))
        assert(!searchList.contains("salmon"))
    }

    @Test
    fun distCorrect() {
        val tree = BKTree()

        assertEquals(tree.dist("some","soft"),2)
    }
}