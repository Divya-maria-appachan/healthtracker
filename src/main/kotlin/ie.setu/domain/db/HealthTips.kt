package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Healthtip.
//       Database wise, this is the table object.
object HealthTips : Table("healthtips") {
    val id = integer("id").primaryKey()
    val tips = varchar("tips", 200)

}

