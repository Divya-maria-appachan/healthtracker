package ie.setu.domain.repository

import ie.setu.domain.Target
import ie.setu.domain.db.Targets
import ie.setu.utils.mapToTarget
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TargetDAO {

    fun findByUserId(userId: Int): List<Target> {
        return transaction {
            Targets
                .select { Targets.userId eq userId }
                .orderBy(Targets.id, SortOrder.DESC)
                .limit(1)
                .map { mapToTarget(it) }

        }
    }

    fun save(target: Target) : Int?{
        return transaction {
            Targets.insert {
                it[targetSleep] = target.targetSleep
                it[targetBmi] = target.targetBmi
                it[date] = target.date
                it[userId] = target.userId
            } get Targets.id
        }
    }
    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Targets.deleteWhere { Targets.userId eq userId }
        }
    }

    fun updateById(id: Int, targetDTO: Target): Int {
        return transaction {
            Targets.update ({
                Targets.userId eq id}) {
                it[targetSleep] = targetDTO.targetSleep
                it[targetBmi] = targetDTO.targetBmi
                it[date] = targetDTO.date
                it[userId] = targetDTO.userId
            }
        }
    }


}