package com.example.searchImages

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mauker.materialsearchview.MaterialSearchView
import br.com.mauker.materialsearchview.db.model.History
import com.example.searchImages.adapter.MainAdapter
import com.example.searchImages.base.BaseActivity
import com.example.searchImages.databinding.ActivityMainBinding
import com.example.searchImages.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy {
        MainAdapter(R.layout.item_list).apply {
            recyclerView = binding.recyclerView
            setEmptyView(R.layout.layout_empty_list)
            isUseEmpty = false
            loadMoreModule.setOnLoadMoreListener {
                loadMoreModule.isEnableLoadMore = true
                viewModel.getSearchImages(binding.searchView.currentQuery)
            }
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
        }
    }

    private val itemDecorator by lazy {
        DividerItemDecoration(cxt, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(cxt, R.drawable.list_divider)!!)
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (searchWrd.isNotBlank()) {
                    searchView.setQuery(searchWrd, false)
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupView()
        setupObserve()
    }

    override fun onStop() {
        super.onStop()
        searchView.onViewStopped()
    }

    override fun onResume() {
        super.onResume()
        searchView.onViewResumed()
    }

    private fun setupView() {
        binding.toolbar.apply {
            searchBtn.setOnClickListener {
                binding.searchView.openSearch()
            }

            changeBtn.setOnClickListener {
                viewModel.isListLayout = !viewModel.isListLayout
                if (viewModel.isListLayout)
                    changeBtn.setImageIcon(Icon.createWithResource(cxt, R.drawable.ic_grid))
                else
                    changeBtn.setImageIcon(Icon.createWithResource(cxt, R.drawable.ic_list))
                switchLayout(viewModel.isListLayout)
            }
        }
        binding.searchView.apply {
            setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.initPage()
                    viewModel.getSearchImages(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })

            setSearchViewListener(object : MaterialSearchView.SearchViewListener {
                override fun onSearchViewOpened() {
                    searchView.visibility = View.VISIBLE
                }

                override fun onSearchViewClosed() {
                    searchView.visibility = View.GONE
                }
            })

            val clickListener = object : MaterialSearchView.OnHistoryItemClickListener {
                override fun onClick(history: History) {
                    searchView.setQuery(history.query, false)
                }

                override fun onLongClick(history: History) {}
            }
            setOnItemClickListener(clickListener)

            adjustTintAlpha(0.8f)
        }
        switchLayout(viewModel.isListLayout)
    }

    private fun switchLayout(isList: Boolean) {
        when (isList) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(cxt)
                recyclerView.addItemDecoration(itemDecorator)
            }
            else -> {
                recyclerView.layoutManager = GridLayoutManager(cxt, 3)
                recyclerView.removeItemDecoration(itemDecorator)
            }
        }
        binding.recyclerView.adapter = adapter
        adapter.switchLayoutType(isList)
    }

    override fun onBackPressed() {
        if (searchView.isOpen) {
            searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupObserve() {
        viewModel.images.observe(this) {
            if (viewModel.page == 1) {
                adapter.isUseEmpty = it.isNullOrEmpty()
                adapter.setList(it)
            } else {
                adapter.addData(it)
            }

        }

        viewModel.isLoadEnd.observe(this) {
            if (it)
                adapter.loadMoreModule.loadMoreEnd(true)
            else
                adapter.loadMoreModule.loadMoreComplete()
        }

        viewModel.callApiFail.observe(this) {
            Toast.makeText(cxt, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}