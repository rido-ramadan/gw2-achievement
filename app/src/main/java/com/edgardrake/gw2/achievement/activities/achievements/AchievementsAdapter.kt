package com.edgardrake.gw2.achievement.activities.achievements

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.Achievement
import com.edgardrake.gw2.achievement.utilities.GlideApp
import com.edgardrake.multipurpose.views.recycler.LoadingViewHolder
import com.edgardrake.multipurpose.views.recycler.PagingRecyclerViewAdapter
import kotlinx.android.synthetic.main.grid_achievement.view.*

class AchievementsAdapter(dataset: MutableList<Achievement>,
                          val defaultIcon: String,
                          val onItemClicked: (Int, Achievement) -> Unit,
                          onScroll: () -> Unit,
                          enablePaging: Boolean):
    PagingRecyclerViewAdapter<Achievement>(dataset, onScroll, enablePaging) {

    override fun getItemViewType(position: Int) =
        if (position < dataset.size)
            R.layout.grid_achievement
        else
            super.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == R.layout.grid_achievement)
            AchievementsHolder(parent)
        else
            super.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AchievementsHolder) {
            val achievement = dataset[holder.adapterPosition]
            holder.title = achievement.name
            holder.setIcon(achievement.icon ?: defaultIcon)
            holder.itemView.setOnClickListener { onItemClicked(holder.adapterPosition, achievement) }
        }
    }

    class AchievementsHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.grid_achievement, parent, false)) {

        var title: String
            get() = itemView.mAchievementTitle.text.toString()
            set(value) { itemView.mAchievementTitle.text = value }

        fun setIcon(url: String) {
            GlideApp.with(itemView)
                .load(url)
                .error(R.drawable.ic_gw2_launcher)
                .into(itemView.mAchievementIcon)
        }
    }
}