package ie.setu.controllers

import ie.setu.domain.repository.AchievementDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object AchievementController {

    private val userDao = UserDAO()
    private val achievementDao = AchievementDAO()


    fun getAchievementsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val achievements = achievementDao.findByUserId(ctx.pathParam("user-id").toInt())
                if (achievements.isNotEmpty()) {
                    ctx.json(achievements)
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

    fun deleteByAchievementId(ctx: Context){
        if (achievementDao.deleteByAchievementId(ctx.pathParam("id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


}