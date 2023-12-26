package ie.setu.domain.db
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Bmies.
//       Database wise, this is the table object.
object Bmies : Table("bmi") {
    val id = integer("id").autoIncrement().primaryKey()
    val weight = double("weight")
    val height = double("height")
    val bmiCalculator = double("bmicalculator")
    val timestamp = datetime("timestamp")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}