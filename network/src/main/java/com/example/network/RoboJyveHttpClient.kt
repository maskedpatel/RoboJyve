package com.example.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Implementation of okhttp client which performs basic calls/parsing and returns data
 * Note I'm aware this isn't necessary for this project, but I decided to at it incase
 * any future follow ups will require network calls
 */
class RoboJyveOkHttpClient(
    /**
     * ok http client
     */
    val okhttpClient: OkHttpClient = OkHttpClientSingleton.getClient()
) : DataRetreverInterface {
    /**
     * @param host hostname of endpoint
     * @param pathSegment path extension of endpoint
     * @param dataObjectCreator lambda which takes a JSONarray from endpoint and parses it into an
     * object of choice (T)
     * @param queries queries in the form (key, value) to add to url
     * @param scheme scheme of call, usually https or http
     * @return list of parsed through elements from given server
     */
    override suspend fun <T> retrieveData(
        host: String,
        pathSegment: String?,
        dataObjectCreator: (JSONObject) -> T,
        queries: List<Pair<String, String>>,
        scheme: String
    ): List<T> {
        return withContext(Dispatchers.IO) {
            // This call will throw error if a bad input.
            // Leaving this crash in place since this is a case we want caught immediately
            val urlBuilder: HttpUrl.Builder = HttpUrl.Builder().scheme(scheme).host(host)
            pathSegment?.let { pathSegment -> urlBuilder.addPathSegment(pathSegment) }
            queries.forEach { (first, second) ->
                urlBuilder.addQueryParameter(first, second)
            }
            val url = urlBuilder.build()
            val request = Request.Builder().url(url).build()
            try {
                val response = okhttpClient.newCall(request).execute()
                return@withContext parseResponse(
                    response,
                    dataObjectCreator
                )
            } catch (e: IOException) {
                Log.e("network", "IO exception $e")
            }
            listOf<T>()
        }
    }

    /**
     * @param response an OKHTTP response object to parse
     * @param dataObjectCreator lambda to parse JSONArray element
     * @return List of parsed elements
     */
    override fun <T> parseResponse(
        response: Response,
        dataObjectCreator: (JSONObject) -> T
    ): List<T> {
        //convert to immutable on parse finish
        val parsedResponse: MutableList<T> = mutableListOf()
        if (!response.isSuccessful) {
            // can use custom logger in larger scale project
            Log.e(
                "network", "Network error code=${response.code} " +
                        "message=${response.message} " +
                        "url=${response.request.url}"
            )
            return parsedResponse
        }
        try {
            val responseData = response.body?.string()
            val jsonDiscoveredItems = JSONArray(responseData)
            for (i in 0 until jsonDiscoveredItems.length()) {
                val currentElement: JSONObject = jsonDiscoveredItems.getJSONObject(i)
                val item = dataObjectCreator(currentElement)
                parsedResponse.add(item)
            }
        } catch (e: JSONException) {
            Log.e(
                "network", "Failed parsing successful response from server " +
                        "error=${e.message} " +
                        "url=${response.request.url} " +
                        "payload=${response.body}"
            )
        }
        return parsedResponse
    }
}