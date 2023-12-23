package ie.setu.domain.repository

import ie.setu.domain.Achievement
import ie.setu.domain.db.Achievements
import ie.setu.utils.mapToAchievement
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class AchievementDAO {

    fun findByUserId(userId: Int): List<Achievement>{
        return transaction {
            Achievements
                .select { Achievements.userId eq userId}
                .map { mapToAchievement(it) }
        }
    }

    fun save(achievement: Achievement): Int {
        return transaction {
            Achievements.insert {
                it[name] = achievement.name
                it[rank] = achievement.rank
                it[date] = achievement.date
                it[userId] =achievement.userId
            } get Achievements.id
        }
    }
    fun deleteByAchievementId (achievementId: Int): Int{
        return transaction{
            Achievements.deleteWhere { Achievements.id eq achievementId }
        }
    }

}