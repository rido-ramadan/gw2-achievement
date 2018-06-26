package com.edgardrake.gw2.achievement.activities.groups

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.edgardrake.gw2.achievement.R
import com.edgardrake.gw2.achievement.activities.categories.AchievementCategoriesActivity
import com.edgardrake.gw2.achievement.https.GuildWars2API
import com.edgardrake.gw2.achievement.library.BaseActivity
import com.edgardrake.gw2.achievement.models.AchievementGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_achievement_category.*
import retrofit2.HttpException

class AchievementGroupsActivity : BaseActivity() {

    private val groups = ArrayList<AchievementGroup>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement_category)

        refreshContainer.setOnRefreshListener { GET_AllAchievementGroups() }

        GET_AllAchievementGroups()
    }

    fun GET_AllAchievementGroups() {
        loading.visibility = View.VISIBLE

        httpCallback = GuildWars2API.getService().GET_AchievementGroups()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> setAchievementGroup(result) },
                { error ->
                    refreshContainer.isRefreshing = false
                    loading.visibility = View.GONE
                    if (error is HttpException) {
                        Log.e("HTTP-Error", "${error.code()}: ${error.message()}")
                    } else{
                        Log.e("Exception", "${error.message}")
                    }
                }
            )
    }

    fun setAchievementGroup(dataset: List<AchievementGroup>) {
        loading.visibility = View.GONE
        refreshContainer.isRefreshing = false

        gridDataset.setHasFixedSize(true)
        if (gridDataset.adapter == null) {
            groups.addAll(dataset)
            gridDataset.adapter = AchievementGroupAdapter(dataset,
                {pos, data -> AchievementCategoriesActivity.startThisActivity(this, data)})
        } else {
            groups.clear()
            groups.addAll(dataset)
            gridDataset.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, AchievementGroupsActivity::class.java))
        }
    }
}
