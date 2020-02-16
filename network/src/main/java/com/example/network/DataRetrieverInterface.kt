package com.example.network

import org.json.JSONObject

/**
 * Interface for a class which retrieves and parses foreign data
 */
interface DataRetreverInterface {
    /**
     * Retrieve data using http paradigm
     */
    suspend fun <T> retrieveData(
        host: String,
        pathSegment: String? = null,
        dataObjectCreator: (JSONObject) -> T,
        queries: List<Pair<String, String>> = listOf(),
        scheme: String = "https"
    ): List<T>

    /**
     * Parse the received response
     */
    fun <T> parseResponse(response: okhttp3.Response, dataObjectCreator: (JSONObject) -> T): List<T>
}