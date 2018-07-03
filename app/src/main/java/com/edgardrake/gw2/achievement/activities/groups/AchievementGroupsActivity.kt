package com.edgardrake.gw2.achievement.activities.groups

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesActivity
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesFragment
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.AchievementGroup
import kotlinx.android.synthetic.main.activity_achievement_group.*
import okhttp3.Headers

class AchievementGroupsActivity : BaseActivity() {

    private var groups = ArrayList<AchievementGroup>()
    private var isCalling = false
    var maxPage: Int? = null
        private set(value) { if (value != null) field = value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_group)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val onClick = {_: Int, data: AchievementGroup -> actionOpenCategory(data)}
        gridDataset.setHasFixedSize(true)
        gridDataset.adapter = AchievementGroupAdapter(groups, onClick)
        gridDataset.addOnScrollListener(onScrollListener)

        refreshContainer.setOnRefreshListener {
            refreshContainer.isRefreshing = false
            groups.clear()
            gridDataset.adapter?.let {
                it as AchievementGroupAdapter
                it.resetLoading()
                it.notifyDataSetChanged()
            }
        }
    }

    private fun GET_AllAchievementGroups() {
        val onSuccess = { result: List<AchievementGroup>, headers: Headers ->
            setAchievementGroup(result)
            maxPage = headers["X-Page-Total"]!!.toInt()
        }
        httpCall(GuildWars2API.getService().GET_AchievementGroups(), onSuccess)
    }

    private fun setAchievementGroup(dataset: List<AchievementGroup>) {
        refreshContainer.isRefreshing = false

        groups.addAll(dataset)
        gridDataset.adapter?.let {
            it as AchievementGroupAdapter
            it.notifyDataSetChanged()
            it.stopLoading()
        }
        gridDataset.addOnScrollListener(onScrollListener)
    }

    private fun actionOpenCategory(group: AchievementGroup) {
        if (achievementGroupDetail != null) {
            setFragment(AchievementCategoriesFragment.newInstance(group),
                "", R.id.achievementGroupDetail, false)
        } else {
            setFragment(AchievementCategoriesFragment.newInstance(group),
                group.name, R.id.fragmentContainer, true)
        }
    }

    private val onScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.adapter?.let {
                val adapter = it as AchievementGroupAdapter
                recyclerView.layoutManager?.let {
                    it as LinearLayoutManager
                    if (!adapter.isStopLoading && !isCalling &&
                        it.itemCount <= it.findFirstVisibleItemPosition() + it.childCount) {
                        recyclerView.removeOnScrollListener(this)
                        GET_AllAchievementGroups()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, AchievementGroupsActivity::class.java))
        }

        const val ACHIEVEMENT_GROUPS = "groups"
    }
}
