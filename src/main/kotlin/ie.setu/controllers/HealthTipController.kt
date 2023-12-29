package ie.setu.controllers

import ie.setu.domain.HealthTip
import ie.setu.domain.repository.HealthTipDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 *  Controller for handling Tips-related operations
 */
object HealthTipController {
    private val healthTipsDao = HealthTipDAO()
    fun addTips(ctx: Context) {
        var healthTip: HealthTip = jsonToObject(ctx.body())
        val id = healthTipsDao.addHealthTip(healthTip)
        if (id != null) {
            ctx.json(healthTip)
            ctx.status(201)
        }
    }

    fun getTips(ctx: Context) {
        val healthTip = healthTipsDao.getRandom()
        if (healthTip != null) {
            ctx.json(healthTip)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getAllTips(ctx: Context) {
        val healthTip = healthTipsDao.getAllTip()
        if (healthTip.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(healthTip)
    }
    fun updateTips(ctx: Context){
        val tip : HealthTip = jsonToObject(ctx.body())
        if ((healthTipsDao.updateTip(tip_id = ctx.pathParam("tip-id").toInt(), healthtip= tip)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    fun deleteTip(ctx: Context){
        if (healthTipsDao.delete(ctx.pathParam("tip-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}