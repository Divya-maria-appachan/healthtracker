package ie.setu.repository


import ie.setu.domain.HealthTip
import ie.setu.domain.db.HealthTips
import ie.setu.domain.repository.HealthTipDAO
import ie.setu.helpers.healthTips
import ie.setu.helpers.populatehealthTipsTable
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

val healthTip1 = healthTips.get(0)
val healthTip2 = healthTips.get(1)
val healthTip3 = healthTips.get(2)



class HealthTipDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadHealthtips {
        @Test
        fun `get all Tips from populated table`() {
            transaction {

                val healthTipDAO = populatehealthTipsTable()

                //Act & Assert
                assertEquals(3, healthTipDAO.getAllTip().size)

            }
        }

        @Test
        fun `get all Tips over empty table`() {
            transaction {

                //Arrange - create and setup UserDAO object
                SchemaUtils.create(HealthTips)
                val healthTipDAO = HealthTipDAO()

                //Act & Assert
                assertEquals(0, healthTipDAO.getAllTip().size)
            }
        }

        @Test
        fun `get random health tip from a table with one entry`() {
            transaction {
                val healthTipDAO = HealthTipDAO()
                SchemaUtils.create(HealthTips)

                healthTipDAO.addHealthTip(healthTip1)

                // Act: Get a random health tip
                val randomHealthTips = healthTipDAO.getRandom()

                // Assert: There should be only one health tip in the result
                assertEquals(1, randomHealthTips.size)
            }
        }
    }

    @Nested
    inner class CreateTips {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populatehealthTipsTable()

                //Act & Assert
                assertEquals(3, userDAO.getAllTip().size)

            }
        }
    }

    @Nested
    inner class UpdateTips {

        @Test
        fun `update a non-existing health tip in the table`() {
            transaction {
                val healthTipDAO = HealthTipDAO()
                SchemaUtils.create(HealthTips)

                // Act: Try to update a non-existing health tip
                val newHealthTip = HealthTip(id = 2, tips = "New tip")
                val updatedRows = healthTipDAO.updateTip(2, newHealthTip)

                // Assert: The update should not affect any rows, and no rows should be updated
                assertEquals(0, updatedRows)
            }
        }
    }
        @Nested
        inner class UpdateHealthTips {
            @Test
            fun `update an existing health tip in the table`() {
                transaction {
                    val healthTipDAO = HealthTipDAO()
                    SchemaUtils.create(HealthTips)

                    // Arrange: Create an initial health tip
                    val initialHealthTip = HealthTip(id = 1, tips = "Initial tip")
                    healthTipDAO.addHealthTip(initialHealthTip)

                    // Act: Update the health tip
                    val newHealthTip = HealthTip(id = 1, tips = "Updated tip")
                    val updatedRows = healthTipDAO.updateTip(1, newHealthTip)

                    // Assert: The update should be successful, and the health tip should be updated
                    assertEquals(1, updatedRows)
                    val updatedHealthTip = healthTipDAO.getRandom().firstOrNull()
                    assertEquals(newHealthTip, updatedHealthTip)
                }
            }

        }

    }



