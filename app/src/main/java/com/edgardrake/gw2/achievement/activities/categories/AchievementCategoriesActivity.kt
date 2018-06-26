package com.edgardrake.gw2.achievement.activities.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.AchievementCategories
import com.edgardrake.gw2.achievement.models.AchievementGroup
import com.edgardrake.gw2.achievement.utilities.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_achievement_categories.*

private const val ACHIEVEMENT_GROUP = "data"

class AchievementCategoriesActivity : BaseActivity() {

    private lateinit var group: AchievementGroup
    private var categories = ArrayList<AchievementCategories>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_categories)

        group = intent.getParcelableExtra<AchievementGroup>(ACHIEVEMENT_GROUP)
        supportActionBar!!.title = group.name

        GET_AchievementCategories(0)
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context, achievementGroup: AchievementGroup) {
            context.startActivity(Intent(context, AchievementCategoriesActivity::class.java)
                .putExtra(ACHIEVEMENT_GROUP, achievementGroup))
        }
    }

    fun GET_AchievementCategories(page: Int = 0) {
        httpCallback = GuildWars2API.getService()
            .GET_AchievementCategories(group.flattenCategories(), page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result ->
                    if (page == 0) categories.clear()
                    categories.addAll(result)

                    val logger = Logger(getActivity())
                    for (category in categories) {
                        logger.addEntry(category.id.toString(), category.name)
                    }
                    logger.show()
                },
                {error -> }
            )
    }
}
