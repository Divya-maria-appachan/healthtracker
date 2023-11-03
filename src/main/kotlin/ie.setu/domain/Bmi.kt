package ie.setu.domain

data class Bmi(
    var id: Int,
    var weight: Double,
    var height: Double,
    var bmi: Double,
    var bmiResult:String,
    var userId: Int

)