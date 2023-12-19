package ie.setu.domain.repository

import ie.setu.domain.Sleep
import ie.setu.domain.db.userSleep
import ie.setu.utils.mapToSleep
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SleepDAO {

    fun findByUserId(userId: Int): List<Sleep>{
        return transaction {
            userSleep
                .select {userSleep.userId eq userId}
                .map {
                    mapToSleep(it)
                }
        }
    }

    fun save(sleep: Sleep) : Int?{
        return transaction {
            userSleep.insert {
                it[duration] = sleep.duration
                it[date] = sleep.date
                it[userId] = sleep.userId
            } get userSleep.id
        }
    }

    fun updateById(sleepId: Int, sleepDTO: Sleep): Int {
        return transaction {
            userSleep.update ({
                userSleep.id eq sleepId}) {
                it[duration] = sleepDTO.duration
                it[date] = sleepDTO.date
                it[userId] = sleepDTO.userId
            }
        }
    }

    fun deleteById (sleepId: Int): Int{
        return transaction{
            userSleep.deleteWhere { userSleep.id eq sleepId }
        }
    }

}