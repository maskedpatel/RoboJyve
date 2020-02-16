package com.example.network

import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.IllegalArgumentException
import kotlin.test.*

class RoboJyveOkHttpClientTest {

    private val response: Response = mockk()
    private val body: ResponseBody = mockk()
    private val mockedUrl: HttpUrl = mockk(relaxed = true)
    private val okHttpClient: OkHttpClient = mockk()
    private val dataObjectCreator: (JSONObject) -> String = { it ->
        it.getString("key")
    }
    private var errorSeen = false
    private val client = RoboJyveOkHttpClient(okHttpClient)

    @BeforeTest
    fun setup() {
        mockkStatic(Log::class)
        errorSeen = false
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } answers {
            errorSeen = true
            0
        }
        every { response.code } returns 0
        every { response.request.url } returns mockedUrl
        every { response.message } returns ""
        every { response.body } returns body
    }

    @Test
    fun testParseResponse() {
        // base verification
        assignResponse(true, "{546}")
        assertEquals(response.isSuccessful, true)
        assertEquals(response.body?.string(), "{546}")

        // branch first if false
        assignResponse(false, "")
        assertEquals(client.parseResponse(response, dataObjectCreator), emptyList())

        // try block throws exception
        every { response.request.url } throws (JSONException(""))
        assignResponse(true, "not a json array")
        assertFailsWith(JSONException::class) {
            client.parseResponse(response, dataObjectCreator)
        }
        every { response.request.url } returns mockedUrl

        // good data for loop size = 0
        assignResponse(true, "[]")
        assertEquals(client.parseResponse(response, dataObjectCreator), emptyList())

        // 1
        assignResponse(true, "[{'key' : '1'}]")
        assertEquals(client.parseResponse(response, dataObjectCreator), listOf("1"))

        // many
        assignResponse(true, "[{'key': '1'}, {'key': '2'}, {'key': '3'}]")
        assertEquals(
            client.parseResponse(response, dataObjectCreator),
            listOf("1", "2", "3")
        )
    }

    @Test
    fun testRetrieveData(): Unit = runBlocking {
        // by using dependency injection we dont actually make any network calls, so url
        // actually existing doesnt matter
        val host = "api.jyve.com"
        val responseStr = "[{'key': '1'}, {'key': '2'}, {'key': '3'}]"
        val output = listOf("1", "2", "3")
        // bad data, imitate bad connection
        every { okHttpClient.newCall(any()) } throws (IOException())
        assertEquals(client.retrieveData(host, null, dataObjectCreator), emptyList())
        assertTrue(errorSeen)
        errorSeen = false

        // good data
        every { okHttpClient.newCall(any()).execute() } returns response
        assignResponse(true, responseStr)
        assertEquals(client.retrieveData(host, null, dataObjectCreator), output)
        Unit

        //bad data, bad inputs
        assertFailsWith(IllegalArgumentException::class) {
            client.retrieveData(host, null, dataObjectCreator, scheme = "s")
        }
        assertFailsWith(IllegalArgumentException::class) {
            client.retrieveData("", null, dataObjectCreator)
        }
        Unit
    }

    private fun assignResponse(isSuccess: Boolean, jsonStr: String) {
        every { response.isSuccessful } returns isSuccess
        every { body.string() } returns jsonStr
    }
}