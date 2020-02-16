package com.example.network

import okhttp3.OkHttpClient

/**
 * Wrapper around okhttp client
 */
object OkHttpClientSingleton {
    private val okHttpClient = OkHttpClient()

    /**
     * returns the client
     */
    fun getClient(): OkHttpClient {
        return okHttpClient
    }
}