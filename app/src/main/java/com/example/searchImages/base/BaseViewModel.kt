package com.example.searchImages.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

typealias Block<T> = suspend (CoroutineScope) -> T
typealias isLoading = suspend (Boolean) -> Unit
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

open class BaseViewModel : ViewModel() {

    protected fun launch(
        block: Block<Unit>,
        isLoading: isLoading,
        error: Error? = null,
        cancel: Cancel? = null,
    ): Job {
        return viewModelScope.launch {
            isLoading.invoke(true)
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        error?.invoke(e)
                    }
                }
            }
            isLoading.invoke(false)
        }
    }

}
