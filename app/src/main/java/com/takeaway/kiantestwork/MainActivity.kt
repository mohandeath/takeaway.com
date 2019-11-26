package com.takeaway.kiantestwork

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.takeaway.kiantestwork.di.DaggerViewModelFactory
import com.takeaway.kiantestwork.ui.adapter.RestauranListAdapter
import com.takeaway.kiantestwork.viewmodels.RestaurantListViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_loading.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    lateinit var viewModel: RestaurantListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RestauranListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        setupViewModel("statuss")
        observeChanges()
    }

    private fun setupViewModel(sortType: String) {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RestaurantListViewModel::class.java)
        viewModel.sortType = sortType

    }

    private fun setupViews() {
        adapter = RestauranListAdapter(this) { restaurant ->
            Toast.makeText(this, "you have clicked on ${restaurant.name}", Toast.LENGTH_LONG).show()
        }
        layoutManager = LinearLayoutManager(this)
        restaurantRecyclerView.adapter = adapter
        restaurantRecyclerView.layoutManager = layoutManager

    }

    private fun observeChanges() {
        viewModel.restaurantList.observe(this, Observer {
            adapter.setItems(it)
        })

        viewModel.loadingVisibility.observe(this, Observer {
            loading.visibility = it!!
        })

        viewModel.retryVisibility.observe(this, Observer {
            retryBtn.visibility = it!!
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, getString(R.string.str_error_general), Toast.LENGTH_LONG).show()


        })

    }

}
