package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Achievements : Table("achievements") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val rank = integer("rank")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}