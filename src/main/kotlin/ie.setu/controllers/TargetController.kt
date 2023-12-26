package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.Target
import ie.setu.domain.repository.TargetDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object TargetController {

    private val userDao = UserDAO()
    private val targetDao = TargetDAO()


    fun getTargetsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val targets = targetDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (targets.isNotEmpty()) {
                ctx.json(targets)
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }


    fun addTarget(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val target : Target = jsonToObject(ctx.body())
        val targetId = targetDao.save(target)
        if (targetId != null) {
            target.id = targetId
            ctx.json(target)
            ctx.status(201)
        }
    }

    fun deleteTargetsByUserId(ctx: Context){
        if (targetDao.deleteByUserId(ctx.pathParam("user-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateTargetsByUserId(ctx: Context){
        val target : Target = jsonToObject(ctx.body())
        if (targetDao.updateById(
                id=ctx.pathParam("user-id").toInt(),
                targetDTO=target) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}