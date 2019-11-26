package com.takeaway.kiantestwork

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.takeaway.kiantestwork.di.DaggerViewModelFactory
import com.takeaway.kiantestwork.viewmodels.RestaurantListViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    lateinit var viewModel: RestaurantListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel("statuss")
    }

    private fun setupViewModel(sortType:String){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RestaurantListViewModel::class.java)
        viewModel.sortType = sortType

    }
}
