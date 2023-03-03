package com.example.myapplicationwithauthorization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationwithauthorization.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_AUTHORIZATION_HEADER = "X-RapidAPI-Key"

class AuthorizarionInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader(
                API_AUTHORIZATION_HEADER,
                "bba9fdc651mshc58bdd6e002bf30p18e392jsnd6652b8ccbd4"
            )
            .build()

        return chain.proceed(newRequest)
    }

}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val logging = HttpLoggingInterceptor()
    val authorizationInterceptor = AuthorizarionInterceptor()
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authorizationInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://trivia-by-api-ninjas.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(TriviaApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logging.level = HttpLoggingInterceptor.Level.BODY
        retrieveDetails()
    }

    private fun setQuestion(question: Data){
        binding.textCategory.text = getString(R.string.category,
            question.firstOrNull()?.category ?: "-"
        )
        binding.textQuestion.text = getString(R.string.question,
            question.firstOrNull()?.question ?: "-"
        )
        binding.textAnswer.text = getString(R.string.answer,
            question.firstOrNull()?.answer ?:"-" )
    }

    private fun retrieveDetails() {
        lifecycleScope.launch {
            try {
                val details = apiService.getQuestion()
                setQuestion(details)
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(R.id.main_view),
                    "Error",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") { retrieveDetails() }.show()
            }
        }
    }
}

