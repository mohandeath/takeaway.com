package com.takeaway.kiantestwork.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * A factory to create ViewModel instances that are compatible with dagger.
 * Using this factory allows injecting dependencies with dagger.
 */
@Suppress("UNCHECKED_CAST")
@Singleton
class DaggerViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        requireNotNull(creator) { "unknown model class $modelClass" }
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}