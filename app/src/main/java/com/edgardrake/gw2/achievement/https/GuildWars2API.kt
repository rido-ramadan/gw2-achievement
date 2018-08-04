package com.edgardrake.gw2.achievement.https

import com.edgardrake.gw2.achievement.models.Achievement
import com.edgardrake.gw2.achievement.models.AchievementCategory
import com.edgardrake.gw2.achievement.models.AchievementGroup
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GuildWars2API {

    @GET("achievements/groups")
    fun GET_AchievementGroups(@Query("page") page: Int): Observable<Response<List<AchievementGroup>>>

    @GET("achievements/categories")
    fun GET_AchievementCategories(@Query("ids") ids: String,
                                  @Query("page") page: Int): Observable<Response<List<AchievementCategory>>>
    @GET("achievements")
    fun GET_Achievements(@Query("ids") ids: String): Observable<Response<List<Achievement>>>

    companion object {
        private val GuildWars2Service by lazy {
            Retrofit.Builder()
                .client(OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.guildwars2.com/v2/")
                .build()
                .create(GuildWars2API::class.java)
        }

        fun getService() : GuildWars2API {
            return GuildWars2Service
        }
    }
}