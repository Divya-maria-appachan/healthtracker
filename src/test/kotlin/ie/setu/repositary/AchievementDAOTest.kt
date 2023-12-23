package ie.setu.repository

import ie.setu.helpers.achievements
import ie.setu.helpers.populateAchievementTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val achievement1 = achievements.get(0)
private val achievement2 = achievements.get(1)
private val achievement3 = achievements.get(2)

class AchievementDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateAchievement {

        @Test
        fun `multiple achievements added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val achievementDAO = populateAchievementTable()
                assertEquals(mutableListOf(achievement1), achievementDAO.findByUserId(achievement1.userId))
                assertEquals(mutableListOf(achievement2), achievementDAO.findByUserId(achievement2.userId))
                assertEquals(mutableListOf(achievement3), achievementDAO.findByUserId(achievement3.userId))
            }
        }
    }

    @Nested
    inner class ReadAchievements {

        @Test
        fun `get achievements by user id that has no badges, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val achievementDAO = populateAchievementTable()
                assertEquals(0, achievementDAO.findByUserId(8999).size)
            }
        }

        @Test
        fun `get badges by user id that exists, results in correct badges returned`() {
            transaction {
                val userDAO = populateUserTable()
                val achievementDAO = populateAchievementTable()
                assertEquals(achievement1, achievementDAO.findByUserId(1).get(0))
                assertEquals(achievement2, achievementDAO.findByUserId(3).get(0))
                assertEquals(achievement3, achievementDAO.findByUserId(2).get(0))
            }
        }
    }
    @Nested
    inner class DeleteGoals {

      @Test
       fun `deleting goals when 1 or more exist for user id results in deletion`() {
          transaction {

              val userDAO = populateUserTable()
             val achivementDAO = populateAchievementTable()

              assertEquals(1, achivementDAO.findByUserId(3).size)
             achivementDAO.deleteByAchievementId(1)
              assertEquals(1, achivementDAO.findByUserId(3).size)
          }
       }
   }

}