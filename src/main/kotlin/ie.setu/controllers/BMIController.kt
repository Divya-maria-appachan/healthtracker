package ie.setu.controllers
import ie.setu.domain.Bmi
import ie.setu.domain.repository.BmiDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object BMIController {
    private var Bmi: Bmi? = null
    fun calculateBmi(ctx: Context) {
        try {
            val newBmiData = jsonToObject<Bmi>(ctx.body())
            val bmi = BmiDAO.calculateBmi(newBmiData.weight, newBmiData.height)
            val Bmiresult =BmiDAO.Bmiresult(bmi)
            ctx.json(mapOf("bmi" to bmi, "Bmiresult" to Bmiresult))
            Bmi = newBmiData
        } catch (e: Exception) {
            ctx.status(400).result("Invalid input: ${e.message}")
        }
    }

}






