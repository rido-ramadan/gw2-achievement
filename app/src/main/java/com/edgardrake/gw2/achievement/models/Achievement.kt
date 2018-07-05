package com.edgardrake.gw2.achievement.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class Achievement(
    val id: Int,
    val name: String,
    var icon: String? = null,
    val description: String?,
    val requirement: String,
    val locked_text: String?,
    val type: AchievementType,
    val flags: List<Flag>?,
    val tiers: List<Tier>? = null,
    val prerequisites: List<Int>? = null,
    val bits: List<Bit>? = null,
    val point_cap: Int? = null
    ): Serializable {

    enum class AchievementType {
        @SerializedName("Default") DEFAULT { override fun toString() = "Default" },
        @SerializedName("ItemSet") ITEM_SET { override fun toString() = "ItemSet" }
    }

    enum class Flag {
        @SerializedName("Pvp") PVP,
        @SerializedName("CategoryDisplay") DISPLAY,
        @SerializedName("MoveToTop") MOVE_TO_TOP,
        @SerializedName("IgnoreNearlyComplete") IGNORE_NEARLY_COMPLETE,
        @SerializedName("Repeatable") REPEATABLE,
        @SerializedName("Hidden") HIDDEN,
        @SerializedName("RequiresUnlock") REQUIRE_UNLOCK,
        @SerializedName("Daily") DAILY,
        @SerializedName("Weekly") WEEKLY,
        @SerializedName("Monthly") MONTHLY,
        @SerializedName("Permanent") PERMANENT
    }

    @Parcelize
    data class Tier(val count: Int, val points: Int): Parcelable, Serializable

    enum class RewardType {
        @SerializedName("Text") TEXT,
        @SerializedName("Item") ITEM,
        @SerializedName("Minipet") MINI_PET,
        @SerializedName("Skin") SKIN
    }

    @Parcelize
    data class Bit(val id: Int,
                   val type: RewardType?,
                   val text: String? = null): Parcelable, Serializable
}