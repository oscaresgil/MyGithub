package com.example.mygithub.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com/"

enum class GithubApiStatus {
    START,
    LOADING,
    ERROR,
    DONE
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getUserAsync] method
 */
interface GithubApiService {
    /**
     * Returns a Coroutine [Deferred] [List] of [GithubUser] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("users/{username}")
    fun getUserAsync(@Path("username", encoded = true) username: String?):
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<GithubUser>

    @GET("/users/{username}/repos")
    fun getReposAsync(@Path("username", encoded = true) username: String?):
            Deferred<List<GithubRepo>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object GithubApi {
    val retrofitService : GithubApiService by lazy { retrofit.create(GithubApiService::class.java) }
}