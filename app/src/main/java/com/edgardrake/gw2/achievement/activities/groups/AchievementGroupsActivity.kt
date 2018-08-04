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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_group)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setFragment(AchievementGroupsFragment.newInstance(), null,
            R.id.fragmentContainer, false)
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, AchievementGroupsActivity::class.java))
        }

        const val ACHIEVEMENT_GROUPS = "groups"
    }
}
