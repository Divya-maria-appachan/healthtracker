package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Targets : Table("targets") {
    val id = integer("id").autoIncrement().primaryKey()
    val targetSleep = double("targetsleep")
    val targetBmi = double("targetbmi")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}