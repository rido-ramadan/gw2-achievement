package com.edgardrake.gw2.achievement.activities.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.AchievementGroup

private const val ACHIEVEMENT_GROUP = "data"

class AchievementCategoriesActivity : BaseActivity() {

    private lateinit var group: AchievementGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_categories)

        group = intent.getParcelableExtra(ACHIEVEMENT_GROUP)
        supportActionBar!!.title = group.name

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, AchievementCategoriesFragment.newInstance(group))
            .commitNow()
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context, achievementGroup: AchievementGroup) {
            context.startActivity(Intent(context, AchievementCategoriesActivity::class.java)
                .putExtra(ACHIEVEMENT_GROUP, achievementGroup))
        }
    }
}
