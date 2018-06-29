package com.edgardrake.gw2.achievement.activities.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseFragment
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.models.AchievementGroup
import com.edgardrake.gw2.achievement.utilities.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_achievement_categories.*
import kotlinx.android.synthetic.main.grid_achievement.*
import retrofit2.HttpException

/**
 * A placeholder fragment containing a simple view.
 */
private const val ACHIEVEMENT_GROUP = "data"

class AchievementCategoriesFragment : BaseFragment() {

    private lateinit var group: AchievementGroup
    private var categories = ArrayList<AchievementCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getParcelable(ACHIEVEMENT_GROUP)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_achievement_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        achievementDesc.text = group.description
        achievementTitle.text = group.name

        if (categories.isEmpty()) {
            GET_AchievementCategories(0)
        }
    }

    private fun GET_AchievementCategories(page: Int = 0) {
        httpCall(GuildWars2API.getService()
            .GET_AchievementCategories(group.flattenCategories(), page),
            {result -> setAchievementCategories(result)})
    }

    private fun setAchievementCategories(source: List<AchievementCategory>) {
        categories.addAll(source)
        gridDataset.setHasFixedSize(true)
        gridDataset.adapter = AchievementCategoriesAdapter(categories,
            {pos, data -> Logger(getHostActivity()).addEntry(data.name, data.description).show()})
    }

    companion object {
        @JvmStatic
        fun newInstance(group: AchievementGroup): AchievementCategoriesFragment =
            AchievementCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ACHIEVEMENT_GROUP, group)
                }
            }
    }
}
