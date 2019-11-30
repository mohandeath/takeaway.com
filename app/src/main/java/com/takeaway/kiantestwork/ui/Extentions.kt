package com.takeaway.kiantestwork.ui.adapter

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

fun SearchView.getOnTextChangeObservable(): Observable<String> {
    val textChangeSubject = PublishSubject.create<String>()
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(query: String): Boolean {
            textChangeSubject.onNext(query)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            textChangeSubject.onComplete()
            return true
        }
    })
    return textChangeSubject
}

