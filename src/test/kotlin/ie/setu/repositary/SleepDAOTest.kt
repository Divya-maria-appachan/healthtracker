package ie.setu.repository

import ie.setu.domain.Sleep
import ie.setu.helpers.populateSleepTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.sleep
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val sleep1 = sleep.get(0)
private val sleep2 = sleep.get(1)
private val sleep3 = sleep.get(2)

class SleepDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateSleep {

        @Test
        fun `multiple weight added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(mutableListOf(sleep1, sleep2), sleepDAO.findByUserId(sleep1.userId))
                assertEquals(mutableListOf(sleep3), sleepDAO.findByUserId(sleep3.userId))
            }
        }
    }

    @Nested
    inner class ReadGoals {

        @Test
        fun `get weight by user id that has no weights, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(0, sleepDAO.findByUserId(3789).size)
            }
        }

        @Test
        fun `get weight by user id that exists, results in a correct weights returned`() {
            transaction {
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(sleep1, sleepDAO.findByUserId(1).get(0))
                assertEquals(sleep2, sleepDAO.findByUserId(1).get(1))
                assertEquals(sleep3, sleepDAO.findByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `updating existing weight in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                val sleep3updated = Sleep(id = 3, duration = 70.0, date = DateTime.now(), userId = 3)
                sleepDAO.updateById(sleep3updated.id, sleep3updated)
                assertEquals(mutableListOf(sleep3updated), sleepDAO.findByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting goals when 1 or more exist for user id results in deletion`() {
            transaction {

                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                assertEquals(1, sleepDAO.findByUserId(3).size)
                sleepDAO.deleteById(3)
                assertEquals(0, sleepDAO.findByUserId(3).size)
            }
        }
    }
}