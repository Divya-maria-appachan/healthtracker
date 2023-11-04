package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.Bmi
import ie.setu.domain.HealthTip
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Bmies
import ie.setu.domain.db.HealthTips
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow


fun mapToUser(it: ResultRow)= User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)
fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)
fun mapToBmi(it: ResultRow) = Bmi(
    id = it[Bmies.id],
    weight = it[Bmies.weight],
    height = it[Bmies.height],
    bmiCalculator = it[Bmies.bmiCalculator],
    timestamp = it[Bmies.timestamp],
    userId = it[Bmies.userId]
)
fun mapToHealthTip(it:ResultRow) = HealthTip(
    id = it[HealthTips.id],
    tips = it[HealthTips.tips]
)







