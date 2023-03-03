package com.example.myapplicationwithauthorization

import retrofit2.http.GET

interface TriviaApiService {
    @GET("/v1/trivia")
    suspend fun getQuestion() : Data
}