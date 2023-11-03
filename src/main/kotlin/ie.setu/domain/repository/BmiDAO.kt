package ie.setu.domain.repository

object BmiDAO {
    fun calculateBmi(weight: Double, height: Double): Double {
        if (weight <= 0 || height <= 0) {
            throw IllegalArgumentException("Weight and height must be greater than zero")
        }

        val heightInMeters = height / 100.0
        return weight / (heightInMeters * heightInMeters)
    }
    fun Bmiresult(bmi: Double): String {
        return when {
            bmi <= 18.5 -> "Underweight"
            bmi <= 24.9 -> "Healthy range"
            bmi <= 29.9 -> "Overweight"
            else -> "Obesity"
        }
    }
}