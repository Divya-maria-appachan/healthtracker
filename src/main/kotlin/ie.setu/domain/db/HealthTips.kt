package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object HealthTips : Table("healthtips") {
    val id = integer("id").primaryKey()
    val tips = varchar("tips", 200)

}

