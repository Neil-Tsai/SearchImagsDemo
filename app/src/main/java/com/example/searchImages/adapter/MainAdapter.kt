package com.example.searchImages.adapter

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.searchImages.App
import com.example.searchImages.R
import com.example.searchImages.model.HitModel

class MainAdapter(
    @LayoutRes layoutResId: Int,
    date: MutableList<HitModel>? = null
) : BaseQuickAdapter<HitModel, BaseViewHolder>(layoutResId, date), LoadMoreModule {

    private var isList = true

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseViewHolder {

        val itemView = when(isList) {
            true -> {
                parent.getItemView(R.layout.item_list)
            }
            else -> {
                parent.getItemView(R.layout.item_grid).apply {
                    val spanCount = 3
                    val iconSize = App.screenWidth / spanCount
                    val params = LinearLayout.LayoutParams(iconSize, iconSize * 3/4)
                    layoutParams = params
                }
            }
        }
        return createBaseViewHolder(itemView)
    }

    override fun convert(holder: BaseViewHolder, item: HitModel) {

        val content = holder.getViewOrNull<TextView>(R.id.contextTv)

        Glide.with(context)
            .load(item.previewURL)
            .into(holder.getView(R.id.typeIv))
        content?.text = item.tags
    }

    fun switchLayoutType(isList: Boolean) {
        this.isList = isList
        notifyItemRangeChanged(0, itemCount)
    }
}
