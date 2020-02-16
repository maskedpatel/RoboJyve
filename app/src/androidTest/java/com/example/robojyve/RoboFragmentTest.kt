package com.example.robojyve

import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import com.example.robojyve.robohash.RoboHashFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

// High level integration/UI test
class RoboFragmentTest {
    companion object {
        const val DEFAULT_DELAY_MS: Long = 2000
    }

    @Test
    fun testDiscoverFragment(): Unit = runBlocking {
        val fragment = launchFragmentInContainer<RoboHashFragment>()
        delay(DEFAULT_DELAY_MS)
        basicUICheck(fragment)
        Unit
    }

    private suspend fun basicUICheck(fragment: FragmentScenario<RoboHashFragment>) {
        val text = UUID.randomUUID().toString()
        var itemCount = 0
        fragment.onFragment {
            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.roborecyclerview)
            val adapter = recyclerView.adapter!!
            itemCount = adapter.itemCount
            val editText = it.view!!.findViewById<AppCompatEditText>(R.id.roborequest)
            val button = it.view!!.findViewById<AppCompatButton>(R.id.robobutton)
            editText.setText(text)
            button.performClick()
        }
        delay(DEFAULT_DELAY_MS)
        fragment.onFragment {
            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.roborecyclerview)
            val adapter = recyclerView.adapter!!
            val updatedCount = adapter.itemCount
            assertEquals(updatedCount, itemCount + 1)
        }
    }
}