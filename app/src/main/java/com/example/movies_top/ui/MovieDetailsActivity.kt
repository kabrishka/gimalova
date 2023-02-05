package com.example.movies_top.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import coil.load
import coil.size.Scale
import com.example.movies_top.R
import com.example.movies_top.api.ApiClient
import com.example.movies_top.api.ApiServices
import com.example.movies_top.databinding.ActivityMovieDetailsBinding
import com.example.movies_top.model.MovieDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieDetailsBinding

    private val api: ApiServices by lazy {
        ApiClient().getClientV22().create(ApiServices::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityId: Int = intent.getIntExtra("fromActivity", 0)
        val movieId: Long = intent.getLongExtra("filmId", 1)

        getMovieInfo(movieId)

        binding.btnBack.setOnClickListener{
            when (activityId) {
                0 -> startActivity(Intent(this, MainActivity::class.java))
                1 -> startActivity(Intent(this, MovieSearchActivity::class.java))
                else -> Log.d(TAG, "Error: an undiscovered Activity")
            }
            finish()
        }

    }

    fun refreshMoviesInfo(id: Long) {
        binding.refreshLayout.setOnRefreshListener {
            getMovieInfo(id)
            binding.refreshLayout.isRefreshing = false
        }
    }


    fun getMovieInfo(id: Long) {
        binding.errorMsg.visibility = View.GONE

        binding.apply {
            prgBarMovies.visibility = View.VISIBLE

            val callMoviesApi = api.getMovieDetails(id)

            callMoviesApi.enqueue(object : Callback<MovieDetails> {

                override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                    Log.e(TAG, "Error : ${response.code()}")

                    prgBarMovies.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            infoMovieLayout.visibility = View.VISIBLE
                            response.body()?.let { itBody ->

                                imgMovie.load(itBody.posterUrl) {
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                imgMovieBack.load(itBody.posterUrl) {
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }

                                tvMovieName.text = itBody.nameRu
                                tvMovieDescription.text = itBody.description
                                tvMovieGenres.text = itBody.genres.joinToString { it.genre }
                                tvMovieCountries.text = itBody.countries.joinToString { it.country }
                                tvMovieDateRelease.text = itBody.year.toString()
                            }
                        }
                        in 300..399 -> {
                            Log.d(TAG, " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d(TAG, " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d(TAG, " Server error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    infoMovieLayout.visibility = View.INVISIBLE
                    errorMsg.visibility = View.VISIBLE
                    refreshMoviesInfo(id)
                    Log.e(TAG, "Error : ${t.message}")
                }
            })
        }
    }

}