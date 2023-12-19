package ie.setu.controllers

import ie.setu.domain.Sleep
import ie.setu.domain.repository.SleepDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object SleepController {

    private val userDao = UserDAO()
    private val sleepDao = SleepDAO()


    fun getSleepByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val sleep = sleepDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (sleep.isNotEmpty()) {
                ctx.json(sleep)
                ctx.status(200)
            }
        }
        else{
            ctx.status(404)
        }
    }


    fun addSleep(ctx: Context) {
        val sleep : Sleep = jsonToObject(ctx.body())
        val sleepId = sleepDao.save(sleep)
        if (sleepId != null) {
            sleep.id = sleepId
            ctx.json(sleep)
            ctx.status(201)
        }
    }


    fun deleteSleepById(ctx: Context){
        if (sleepDao.deleteById(ctx.pathParam("id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    fun updateSleepById(ctx: Context){
        val sleep : Sleep = jsonToObject(ctx.body())
        if (sleepDao.updateById(
                sleepId=ctx.pathParam("id").toInt(),
                sleepDTO=sleep) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}