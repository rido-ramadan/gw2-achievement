package com.edgardrake.gw2.achievement.activities.groups

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesActivity
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesFragment
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.AchievementGroup
import com.edgardrake.gw2.achievement.utilities.Logger
import kotlinx.android.synthetic.main.activity_achievement_group.*
import okhttp3.Headers

class AchievementGroupsActivity : BaseActivity() {

    private val groups = ArrayList<AchievementGroup>()
    var maxPage: Int? = null
        private set(value) { if (value != null) field = value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_group)
        refreshContainer.setOnRefreshListener { GET_AllAchievementGroups() }
        if (groups.isEmpty()) {
            GET_AllAchievementGroups()
        } else {
            setAchievementGroup(groups)
        }
    }

    private fun GET_AllAchievementGroups() {
        loading.visibility = View.VISIBLE

        if (!groups.isEmpty()) {
            groups.clear()
            gridDataset.adapter?.notifyDataSetChanged()
        }

        val onSuccess = { result: List<AchievementGroup>, headers: Headers ->
            setAchievementGroup(result)
            maxPage = headers["X-Page-Total"]!!.toInt()
        }
        httpCall(GuildWars2API.getService().GET_AchievementGroups(), onSuccess)
    }

    private fun setAchievementGroup(dataset: List<AchievementGroup>) {
        loading.visibility = View.GONE
        refreshContainer.isRefreshing = false

        gridDataset.setHasFixedSize(true)
        if (gridDataset.adapter == null) {
            groups.addAll(dataset)
            gridDataset.adapter = AchievementGroupAdapter(groups,
                {pos, data -> actionOpenCategory(data)})
        } else {
            groups.addAll(dataset)
            gridDataset.adapter?.notifyDataSetChanged()
        }
    }

    private fun actionOpenCategory(group: AchievementGroup) {
        if (achievementGroupDetail != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.achievementGroupDetail, AchievementCategoriesFragment.newInstance(group))
                .commitNow()
        } else {
            AchievementCategoriesActivity.startThisActivity(this, group)
        }
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, AchievementGroupsActivity::class.java))
        }
    }
}
