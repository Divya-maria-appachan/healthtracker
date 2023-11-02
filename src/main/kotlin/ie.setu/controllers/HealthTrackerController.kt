package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }


    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email").toString())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun deleteUser(ctx: Context) {
        val user = userDao.delete(ctx.pathParam("user-id").toInt())
        ctx.json(user)
    }

    fun updateUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val userUpdates = mapper.readValue<User>(ctx.body())
        userDao.update(
            id = ctx.pathParam("user-id").toInt(),
            user = userUpdates
        )
    }
    //--------------------------------------------------------------
    // ActivityDAO specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        val activities = activityDAO.getAll()
        ctx.json(jsonObjectMapper().writeValueAsString(activities))
    }



    fun addActivity(ctx: Context) {
        val activity = jsonToObject<Activity>(ctx.body())
        activityDAO.save(activity)
        ctx.json(activity)
    }
    fun deleteActivityByActivityId(ctx: Context){
        activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt())
    }
    fun updateActivity(ctx: Context){

        val activity =jsonToObject<Activity>(ctx.body())
        activityDAO.updateByActivityId(
            activityId = ctx.pathParam("activity-id").toInt(),
            activityDTO=activity)
    }


    fun getActivityById(ctx: Context) {
        val activityId = ctx.pathParam("activity-id").toInt()
        val activity = activityDAO.findByActivityId(activityId)

        if (activity != null) {
            ctx.json(jsonObjectMapper().writeValueAsString(activity))
        } else {
            ctx.json("Activity not found")
        }
    }
    fun getActivitiesByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        if (userDao.findById(userId) != null) {
            val activities = activityDAO.findByUserId(userId)
            if (activities.isNotEmpty()) {
                ctx.json(jsonObjectMapper().writeValueAsString(activities))
            }
        }
    }


    fun deleteActivityByUserId(ctx: Context){
        activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

}


