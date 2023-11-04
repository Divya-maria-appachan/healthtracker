package ie.setu.controllers

import ie.setu.domain.HealthTip
import ie.setu.domain.repository.HealthTipDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context


object HealthTipController{
    private val healthTipsDao = HealthTipDAO()
    fun addTips(ctx: Context) {
        var healthTip: HealthTip = jsonToObject(ctx.body())
        val id = healthTipsDao.addHealthTip(healthTip)
        if (id != null) {
            ctx.json(healthTip)
            ctx.status(201)
        }
    }
    fun getTips(ctx: Context){
        val healthTip = healthTipsDao.getRandom()
        if (healthTip != null) {
            ctx.json(healthTip)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }

    }
}