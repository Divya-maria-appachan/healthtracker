package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.Target
import ie.setu.domain.db.*
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
fun mapToSleep(it: ResultRow) = Sleep(
    id = it[userSleep.id],
    duration = it[userSleep.duration],
    date = it[userSleep.date],
    userId = it[userSleep.userId]
)
fun mapToAchievement(it: ResultRow) = Achievement(
    id = it[Achievements.id],
    name = it[Achievements.name],
    rank = it[Achievements.rank],
    date = it[Achievements.date],
    userId = it[Achievements.userId]
)
fun mapToTarget(it: ResultRow) = Target(
    id = it[Targets.id],
    targetSleep = it[Targets.targetSleep],
    targetBmi = it[Targets.targetBmi],
    date = it[Targets.date],
    userId = it[Targets.userId]
)












