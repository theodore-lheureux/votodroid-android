package com.theodorelheureux.votodroid_android.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.theodorelheureux.votodroid_android.MyApp
import okhttp3.OkHttpClient

object GQLClient {

    private val cookieJar =
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MyApp.getContext()))

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        }.cookieJar(cookieJar)
        .build()

    val instance: ApolloClient =
        ApolloClient.Builder().serverUrl("https://server.theodorelheureux.com/votodroid")
            .okHttpClient(
                okHttpClient
            ).build()
}