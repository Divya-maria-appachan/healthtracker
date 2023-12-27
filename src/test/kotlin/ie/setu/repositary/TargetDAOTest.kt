package ie.setu.repository

import ie.setu.domain.Target
import ie.setu.helpers.populateTargetTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.targets
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val target1 = targets.get(0)
private val target2 = targets.get(1)
private val target3 = targets.get(2)

class TargetDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateGoals {

        @Test
        fun `multiple targets added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val targetDAO = populateTargetTable()
                assertEquals(mutableListOf(target1), targetDAO.findByUserId(target1.userId))
                assertEquals(mutableListOf(target2), targetDAO.findByUserId(target2.userId))
                assertEquals(mutableListOf(target3), targetDAO.findByUserId(target3.userId))
            }
        }
    }

    @Nested
    inner class ReadTargets {

        @Test
        fun `get goal by user id that has no goals, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val targetDAO = populateTargetTable()
                assertEquals(0, targetDAO.findByUserId(3789).size)
            }
        }

        @Test
        fun `get goal by user id that exists, results in a correct goals returned`() {
            transaction {
                val userDAO = populateUserTable()
                val targetDAO = populateTargetTable()
                assertEquals(target1, targetDAO.findByUserId(1).get(0))
                assertEquals(target2, targetDAO.findByUserId(2).get(0))
                assertEquals(target3, targetDAO.findByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateTargets {

        @Test
        fun `updating existing target in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val targetDAO = populateTargetTable()

                val target3updated = Target(id = 3, targetSleep = 7.00, targetBmi = 20.00,
                    date = DateTime.now(), userId = 3)
                targetDAO.updateById(target3updated.id, target3updated)
                assertEquals(mutableListOf(target3updated), targetDAO.findByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteTargets {

        @Test
        fun `deleting targets when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val targetDAO = populateTargetTable()

                //Act & Assert
                assertEquals(1, targetDAO.findByUserId(3).size)
                targetDAO.deleteByUserId(3)
                assertEquals(0, targetDAO.findByUserId(3).size)
            }
        }
    }
}