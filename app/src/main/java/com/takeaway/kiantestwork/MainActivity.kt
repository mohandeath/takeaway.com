package com.takeaway.kiantestwork

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.takeaway.kiantestwork.data.dto.SortType
import com.takeaway.kiantestwork.di.DaggerViewModelFactory
import com.takeaway.kiantestwork.ui.adapter.RestauranListAdapter
import com.takeaway.kiantestwork.ui.adapter.getOnTextChangeObservable
import com.takeaway.kiantestwork.viewmodels.RestaurantListViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_loading.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    private lateinit var viewModel: RestaurantListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RestauranListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        setupViewModel()
        observeChanges()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.actionSearch)
        val searchView = searchItem?.actionView as SearchView
        setupSearchViewBehaviour(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     *  used Rx to reduce irrelevant calls, this can saves lots of
     *  unnecessary calls to repository and it will be really helpful
     *  specially in case that we want to make searches from a network calls in a real-world app
     */
    private fun setupSearchViewBehaviour(searchView: SearchView) {
        searchView.queryHint = getString(R.string.searchHint)
        searchView.getOnTextChangeObservable()
            .debounce(10, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                viewModel.query = query
            }.addTo(CompositeDisposable())


    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RestaurantListViewModel::class.java)
        viewModel.sortType = SortType.DEFAULT_STATUS

    }

    private fun setupViews() {
        adapter = RestauranListAdapter(this) { restaurant ->
            if (!restaurant.isFavorite)
                viewModel.addRestaurantToFavorites(restaurant)
            else
                viewModel.removeRestaurantFromFavorites(restaurant)

        }
        layoutManager = LinearLayoutManager(this)
        restaurantRecyclerView.adapter = adapter
        restaurantRecyclerView.layoutManager = layoutManager
        btnSort.setOnClickListener { showSortingDialog() }

    }

    private fun observeChanges() {
        viewModel.restaurantList.observe(this, Observer {
            adapter.setItems(it, viewModel.sortType)
            restaurantRecyclerView.smoothScrollToPosition(0)

        })

        viewModel.loadingVisibility.observe(this, Observer {
            loading.visibility = it!!

        })

        viewModel.retryVisibility.observe(this, Observer {
            retryBtn.visibility = it!!
        })

        viewModel.errorMessage.observe(this, Observer {
            Snackbar.make(
                restaurantRecyclerView,
                getString(R.string.str_error_general),
                Snackbar.LENGTH_LONG
            ).show()

        })
        viewModel.infoMessage.observe(this, Observer { message ->
            Snackbar.make(
                restaurantRecyclerView,
                message,
                Snackbar.LENGTH_LONG
            ).show()

        })

    }

    private fun showSortingDialog() {
        val listItems = viewModel.sorting.map { it.key }.toTypedArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.sortDialogueTitle))
        val currentItem = viewModel.getCurrentSortIndex()
        builder.setSingleChoiceItems(listItems, currentItem) { dialog, selected ->
            viewModel.sortType = viewModel.sorting[selected]
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}
