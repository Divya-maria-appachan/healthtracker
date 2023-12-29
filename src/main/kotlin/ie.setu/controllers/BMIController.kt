package ie.setu.controllers
import ie.setu.domain.Bmi
import ie.setu.domain.repository.BmiDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
/**
 *  Controller for handling Bmi-related operations
 */
object BMIController {
    private val userDao = UserDAO()
    private var bmiDAO = BmiDAO()
    fun calculateBmi(ctx: Context) {
        val bmi: Bmi = jsonToObject(ctx.body())
        val userId = userDao.findById(bmi.userId)
        if (userId != null) {
            val bmiId = bmiDAO.save(bmi)
            bmi.id = bmiId
            ctx.json(bmi)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun getAllBmi(ctx: Context) {
        val bimes = bmiDAO.getAll()
        if (bimes.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(bimes)
    }

    fun getByBmiId(ctx: Context) {
        val bimes = bmiDAO.findByBmiId((ctx.pathParam("bmi-id").toInt()))
        if (bimes != null) {
            ctx.json(bimes)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
    fun deleteBmiId(ctx: Context){
        if (bmiDAO.deleteByBmiId(ctx.pathParam("bmi-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    fun getBmiByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val bmies = bmiDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (bmies.isNotEmpty()) {
                ctx.json(bmies)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }
    fun deleteBmiByUserId(ctx: Context){
        if (bmiDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}