package ie.setu.domain.repository


import ie.setu.domain.HealthTip
import ie.setu.domain.db.HealthTips
import ie.setu.utils.mapToHealthTip
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
        val tablesize = getAllTip().size
        if (tablesize > 1) {
            val randomId = Random.nextInt(1, tablesize)
            return transaction {
                HealthTips
                    .select { HealthTips.id eq randomId }
                    .map { mapToHealthTip(it) }
            }
        } else {
            return transaction {
                HealthTips
                    .select { HealthTips.id eq 1 }
                    .map { mapToHealthTip(it) }
            }
        }
    }
        fun getAllTip(): ArrayList<HealthTip> {
            val HealthTipList: ArrayList<HealthTip> = arrayListOf()
            transaction {
                HealthTips.selectAll().map {
                    HealthTipList.add(mapToHealthTip(it))
                }
            }
            return HealthTipList
        }

        fun updateTip(tip_id: Int, healthtip: HealthTip): Int {
            return transaction {
                HealthTips.update({
                    HealthTips.id eq tip_id
                }) {
                    it[id] = healthtip.id
                    it[tips] = healthtip.tips
                }
            }
        }


    }








