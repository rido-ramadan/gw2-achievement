package com.edgardrake.gw2.achievement.activities.groups


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesFragment
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseFragment
import com.edgardrake.gw2.achievement.models.AchievementGroup
import kotlinx.android.synthetic.main.fragment_achievement_group.*
import okhttp3.Headers

/**
 * A simple [Fragment] subclass.
 * Use the [AchievementGroupsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AchievementGroupsFragment : BaseFragment() {

    private var groups = ArrayList<AchievementGroup>()
    private var isCalling = false
    var maxPage: Int? = null
        private set(value) { if (value != null) field = value }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_achievement_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        httpClient.call(GuildWars2API.getService().GET_AchievementGroups(), onSuccess)
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
        if (hostActivity.findViewById<ViewGroup>(R.id.achievementGroupDetail) != null) {
            hostActivity.setFragment(AchievementCategoriesFragment.newInstance(group),
                "", R.id.achievementGroupDetail, false)
        } else {
            hostActivity.setFragment(AchievementCategoriesFragment.newInstance(group),
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AchievementGroupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = AchievementGroupsFragment()
    }
}
