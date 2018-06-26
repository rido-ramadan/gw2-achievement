package com.edgardrake.gw2.achievement.models

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AchievementGroup(
    val id: String,
    val name: String,
    val description: String,
    val order: Int,
    val categories: List<Int>
): Parcelable {
    fun flattenCategories(): String {
        return TextUtils.join(",", categories)
    }
}