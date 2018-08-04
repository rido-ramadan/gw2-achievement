package com.edgardrake.gw2.achievement.activities.groups

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.AchievementGroup
import com.edgardrake.gw2.achievement.widgets.recycler.PagingRecyclerViewAdapter
import kotlinx.android.synthetic.main.grid_achievement_group.view.*

class AchievementGroupAdapter(dataset: MutableList<AchievementGroup>,
                              val onItemClicked: (Int, AchievementGroup) -> Unit,
                              onScroll: () -> Unit,
                              enablePaging: Boolean):
    PagingRecyclerViewAdapter<AchievementGroup>(dataset, onScroll, enablePaging) {

    override fun getItemViewType(position: Int): Int =
        if (position < dataset.size)
            R.layout.grid_achievement_group
        else
            super.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (viewType == R.layout.grid_achievement_group)
            AchievementGroupHolder(parent)
        else
            super.onCreateViewHolder(parent, viewType)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AchievementGroupHolder) {
            val achievementGroup = dataset[holder.adapterPosition]
            holder.title = achievementGroup.name
            holder.description = achievementGroup.description
            holder.itemView.setOnClickListener { onItemClicked(holder.adapterPosition, achievementGroup) }
        }
    }

    class AchievementGroupHolder(parent: ViewGroup) : ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.grid_achievement_group, parent, false)) {

        var title: String
            get() = itemView.achievementGroupName.text.toString()
            set(value) { itemView.achievementGroupName.text = value }

        var description: String
            get() = itemView.achievementGroupDesc.text.toString()
            set(value) { itemView.achievementGroupDesc.text = value }
    }
}

