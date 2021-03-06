package com.gilbertohdz.android.politicalpreparedness.network

import com.gilbertohdz.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.gilbertohdz.android.politicalpreparedness.network.models.ElectionResponse
import com.gilbertohdz.android.politicalpreparedness.network.models.RepresentativeResponse
import com.gilbertohdz.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// DONE: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
fun provideMoshi(): Moshi {
    return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter()) // <-- https://knowledge.udacity.com/questions/500881
            .add(ElectionAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
}

fun provideRetrofit(moshi: Moshi): Retrofit {
    return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(CivicsHttpClient.getClient())
            .baseUrl(BASE_URL)
            .build()
}

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

fun provideCivicsApiService(retrofit: Retrofit): CivicsApiService = retrofit.create(CivicsApiService::class.java)

interface CivicsApiService {
    // DONE: Add elections API Call
    @GET("elections")
    suspend fun getElections(): Response<ElectionResponse>

    // DONE: Add voterinfo API Call
    @GET("voterinfo")
    suspend fun getVoterInfo(@Query("address") address: String, @Query("electionId") electionId: Int): Response<VoterInfoResponse>

    // DONE: Add representatives API Call
    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String): Response<RepresentativeResponse>
}