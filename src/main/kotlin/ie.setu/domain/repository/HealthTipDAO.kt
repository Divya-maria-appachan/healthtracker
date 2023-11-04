package ie.setu.domain.repository


import ie.setu.domain.HealthTip
import ie.setu.domain.db.HealthTips
import ie.setu.utils.mapToHealthTip
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random


class HealthTipDAO {
    // Add a new health tip to the database
    fun addHealthTip(healthtip: HealthTip): Int? {
        return transaction {
            HealthTips.insert {
                it[id] = healthtip.id
                it[tips] = healthtip.tips
            } get HealthTips.id
        }
    }

    fun getRandom(): List<HealthTip> {
        val randomId = Random.nextInt(1, 10)
        return transaction {
            HealthTips
                .select { HealthTips.id eq randomId }
                .map { mapToHealthTip(it) }


        }
    }


}








