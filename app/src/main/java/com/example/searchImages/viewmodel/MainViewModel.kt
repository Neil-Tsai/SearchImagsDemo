package com.example.searchImages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.searchImages.base.BaseViewModel
import com.example.searchImages.model.HitModel
import com.example.searchImages.service.PixabayApi

class MainViewModel : BaseViewModel() {

    var isListLayout = true
    private val count = 50
    var page = 0

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _totalHits = MutableLiveData<Int>()
    val isLoadEnd: LiveData<Boolean> = Transformations.map(_totalHits) {
        var maxPages = it / count
        if (maxPages > 0)
            maxPages++
        maxPages == page
    }

    private val _callApiFail = MutableLiveData<Exception>()
    val callApiFail: LiveData<Exception>
        get() = _callApiFail

    private val _images = MutableLiveData<List<HitModel>>()
    val images: LiveData<List<HitModel>>
        get() = _images

    fun initPage() {
        page = 0
    }

    fun getSearchImages(query: String?) {
        page++
        launch(
            block = {
                 val data = PixabayApi.getSearchImages(
                    query = query,
                    count = count,
                    page = page
                )
                _totalHits.value = data.totalHits
                _images.value = data.hits
            },
            error = {
                it.printStackTrace()
                _callApiFail.value = it
            },
            isLoading = {
                if (page < 2)
                    _isLoading.value = it
            }
        )
    }
}