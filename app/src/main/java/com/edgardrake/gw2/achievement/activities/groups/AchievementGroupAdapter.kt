package com.edgardrake.gw2.achievement.activities.groups

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.models.AchievementGroup
import kotlinx.android.synthetic.main.grid_achievement_group.view.*

class AchievementGroupAdapter(val dataset: List<AchievementGroup>,
                              val onItemClicked: (Int, AchievementGroup) -> Unit):
    RecyclerView.Adapter<AchievementGroupAdapter.AchievementGroupHolder>() {

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementGroupHolder {
        return AchievementGroupHolder(parent)
    }

    override fun onBindViewHolder(holder: AchievementGroupHolder, position: Int) {
        val achievementGroup = dataset[holder.adapterPosition]
        holder.title = achievementGroup.name
        holder.description = achievementGroup.description
        holder.itemView.setOnClickListener {onItemClicked(holder.adapterPosition, achievementGroup)}
    }

    class AchievementGroupHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.grid_achievement_group, parent, false)) {

        var title: String
            get() = itemView.achievementGroupName.text.toString()
            set(value) { itemView.achievementGroupName.text = value }

        var description: String
            get() = itemView.achievementGroupDesc.text.toString()
            set(value) { itemView.achievementGroupDesc.text = value }
    }
}

