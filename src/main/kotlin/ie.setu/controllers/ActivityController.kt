package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.Achievement
import ie.setu.domain.Activity
import ie.setu.domain.repository.AchievementDAO
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import org.joda.time.LocalDateTime

/**
 *  Controller for handling Activity-related operations
 */
object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val achievementDao = AchievementDAO()
    val ranks = mutableListOf<Int>()
    fun getAllActivities(ctx: Context) {
        val activities = activityDAO.getAll()
        if (activities.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(activities)
    }

    fun saveAchievement(rank: Int, achievement: Achievement) {
        if (rank !in ranks) {
            achievementDao.save(achievement)

        }
    }

        fun getActivitiesByUserId(ctx: Context) {
            if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
                val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
                if (activities.isNotEmpty()) {
                    var totalduration = 0.0
                    for (activity in activities) {
                        totalduration += activity.duration
                    }
                    val achievement1: Achievement = jsonToObject(
                        """{"name": "60 ms", "rank": 1, "date": "${LocalDateTime.now()}", "userId": ${
                            ctx.pathParam("user-id")
                        }}"""
                    )
                    val achievement2: Achievement = jsonToObject(
                        """{"name": "120 ms", "rank": 2, "date": "${LocalDateTime.now()}", "userId": ${
                            ctx.pathParam("user-id")
                        }}"""
                    )
                    val achievement3: Achievement = jsonToObject(
                        """{"name": "180 ms", "rank": 3, "date": "${LocalDateTime.now()}", "userId": ${
                            ctx.pathParam("user-id")
                        }}"""
                    )
                    val achievement4: Achievement = jsonToObject(
                        """{"name": "240 ms", "rank": 4, "date": "${LocalDateTime.now()}", "userId": ${
                            ctx.pathParam("user-id")
                        }}"""
                    )

                    val achievements = achievementDao.findByUserId(ctx.pathParam("user-id").toInt())

                    if (achievements.isNotEmpty()) {
                        for (achievement in achievements) {
                            ranks.add(achievement.rank)
                        }
                    }

                    if (totalduration >= 20.0 && totalduration < 60.0) {
                        saveAchievement(1, achievement1)
                    } else if (totalduration >= 60.0 && totalduration < 120.0) {
                        saveAchievement(1, achievement1)
                        saveAchievement(2, achievement2)
                    } else if (totalduration >= 120.0 && totalduration < 180.0) {
                        saveAchievement(1, achievement1)
                        saveAchievement(2, achievement2)
                        saveAchievement(3, achievement3)
                    } else if (totalduration >= 240.0) {
                        saveAchievement(1, achievement1)
                        saveAchievement(2, achievement2)
                        saveAchievement(3, achievement3)
                        saveAchievement(4, achievement4)
                    }

                    val mapper = jacksonObjectMapper()
                        .registerModule(JodaModule())
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    ctx.json(mapper.writeValueAsString(activities))
                    ctx.status(200)
                } else {
                    ctx.status(404)
                }
            } else {
                ctx.status(404)
            }
        }

        fun getActivityById(ctx: Context) {
            val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
            if (activity != null) {
                ctx.json(activity)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        }

        fun addActivity(ctx: Context) {
            val activity: Activity = jsonToObject(ctx.body())
            val userId = userDao.findById(activity.userId)
            if (userId != null) {
                val activityId = activityDAO.save(activity)
                activity.id = activityId
                ctx.json(activity)
                ctx.status(201)
            } else {
                ctx.status(404)
            }
        }

        fun deleteActivityByActivityId(ctx: Context) {
            if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0)
                ctx.status(204)
            else
                ctx.status(404)
        }

        fun deleteActivityByUserId(ctx: Context) {
            if (activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
                ctx.status(204)
            else
                ctx.status(404)
        }

        fun updateActivity(ctx: Context) {
            val activity: Activity = jsonToObject(ctx.body())
            if (activityDAO.updateByActivityId(
                    activityId = ctx.pathParam("activity-id").toInt(),
                    activityToUpdate = activity
                ) != 0
            )
                ctx.status(204)
            else
                ctx.status(404)
        }
    }






