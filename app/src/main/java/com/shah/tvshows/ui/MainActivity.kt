package com.shah.tvshows.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shah.tvshows.databinding.ActivityMainBinding
import com.shah.tvshows.util.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeObserver()

        setSearchSubmitListener()
    }

    private fun setSearchSubmitListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.getTvShow(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            viewModel.tvShow.collectLatest { state ->
                when(state) {
                    is Resource.Loading -> {
                        Log.i("API Response: ", "Loading...")
                    }
                    is Resource.Success -> {
                        Log.i("API Response: ", "Success")
                        Picasso.get()
                            .load(state.data?.image?.original)
                            .into(binding.showImage)
                        binding.showName.text = state.data?.name
                        binding.showPremiered.text = "Premiered: ${state.data?.premiered}"
                    }
                    is Resource.Error -> {
                        Log.i("API Response: ", "Error -> ${state.message}")
                        Toast.makeText(this@MainActivity, "Sorry, no results found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}