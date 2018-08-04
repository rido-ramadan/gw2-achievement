package com.edgardrake.gw2.achievement.widgets.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.multipurpose.utilities.inflate

class LoadingViewHolder(parent: ViewGroup):
    RecyclerView.ViewHolder(parent.inflate(LAYOUT_ID)) {

    companion object {
        const val LAYOUT_ID: Int = R.layout.grid_loading_view_holder
    }
}