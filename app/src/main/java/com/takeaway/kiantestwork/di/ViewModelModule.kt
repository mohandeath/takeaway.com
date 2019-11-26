package com.takeaway.kiantestwork.di


import androidx.lifecycle.ViewModel
import com.takeaway.kiantestwork.viewmodels.RestaurantListViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {



    @Binds
    @IntoMap
    @ViewModelKey(RestaurantListViewModel::class)
    internal abstract fun provideRestaurantListViewModel(viewModel: RestaurantListViewModel): ViewModel

}
