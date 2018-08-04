package com.edgardrake.gw2.achievement.activities.groups


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesFragment
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.multipurpose.base.PagingFragment
import com.edgardrake.gw2.achievement.models.AchievementGroup
import kotlinx.android.synthetic.main.fragment_achievement_group.*
import okhttp3.Headers

/**
 * A simple [Fragment] subclass.
 * Use the [AchievementGroupsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AchievementGroupsFragment : PagingFragment() {

    override val dataset = mutableListOf<AchievementGroup>()

    private var maxPage: Int? = null
        private set(value) { if (value != null) field = value }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_achievement_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClick = {_: Int, data: AchievementGroup -> actionOpenCategory(data)}
        val onScroll = { GET_AllAchievementGroups() }

        adapter = AchievementGroupAdapter(dataset, onClick, onScroll, isPagingEnabled)
            .apply { attachTo(gridDataset) }

        refreshContainer.setOnRefreshListener {
            refreshContainer.isRefreshing = false
            adapterReset()
        }
    }

    private fun GET_AllAchievementGroups() {
        val onSuccess = { result: List<AchievementGroup>, headers: Headers ->
            setAchievementGroup(result)
            maxPage = headers["X-Page-Total"]!!.toInt()

            currentPage++
            if (currentPage >= maxPage!!) {
                adapterStop()
            }
        }
        httpClient.call(GuildWars2API.getService().GET_AchievementGroups(currentPage), onSuccess)
    }

    private fun setAchievementGroup(source: List<AchievementGroup>) {
        refreshContainer.isRefreshing = false
        dataset.addAll(source)

        adapter.run {
            onNextPageLoaded()
            notifyDataSetChanged()
        }
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
