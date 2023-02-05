package com.example.movies_top.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_top.adapter.MovieAdapter
import com.example.movies_top.api.ApiClient
import com.example.movies_top.api.ApiServices
import com.example.movies_top.databinding.ActivityMovieSearchBinding
import com.example.movies_top.model.MoviesTopList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieSearchBinding

    private val moviesAdapter by lazy { MovieAdapter() }

    private val api: ApiServices by lazy {
        ApiClient().getClientV21().create(ApiServices::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.errorMsg.visibility = View.GONE
        binding.prgBarMovies.visibility = View.GONE

        binding.etMovie.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener getMovies()
            }
            return@setOnEditorActionListener false
        }

        binding.btnBack.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun refreshMoviesList() {
        binding.refreshLayout.setOnRefreshListener {
            getMovies()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun getMovies():Boolean {
        binding.errorMsg.visibility = View.GONE
        binding.prgBarMovies.visibility = View.GONE

        val movieName = binding.etMovie.text.toString()

        binding.apply {
            prgBarMovies.visibility = View.VISIBLE

            val callMoviesApi = api.getMoviesByKeyword(movieName,1)

            callMoviesApi.enqueue(object : Callback<MoviesTopList> {

                override fun onResponse(call: Call<MoviesTopList>, response: Response<MoviesTopList>) {

                    prgBarMovies.visibility = View.GONE

                    when (response.code()) {
                        in 200..299 -> {
                            Log.d(TAG, "Success messages : ${response.code()}")
                            response.body()?.let { itBody ->
                                itBody.films.let { itData ->
                                    if (itData.isNotEmpty()) {
                                        moviesAdapter.differ.submitList(itData)
                                        //Recycler
                                        rlMovies.apply {
                                            layoutManager = LinearLayoutManager(this@MovieSearchActivity)
                                            adapter = moviesAdapter
                                        }
                                    }
                                }
                            }
                        }
                        in 300..399 -> {
                            Log.d(TAG, "Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d(TAG, "Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d(TAG, "Server error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<MoviesTopList>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    errorMsg.visibility = View.VISIBLE
                    refreshMoviesList()
                    Log.e(TAG, "Error : ${t.message}")
                }
            })
        }
        return true
    }
}