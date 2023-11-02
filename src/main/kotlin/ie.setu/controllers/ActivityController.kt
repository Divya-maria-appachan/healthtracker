package ie.setu.controllers


import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
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

        val activity = jsonToObject<Activity>(ctx.body())
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
