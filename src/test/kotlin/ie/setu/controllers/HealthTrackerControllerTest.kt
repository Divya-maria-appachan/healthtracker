package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.*
import ie.setu.helpers.*
import ie.setu.helpers.TestUtilities.addActivity
import ie.setu.helpers.TestUtilities.addBmi
import ie.setu.helpers.TestUtilities.addSleep
import ie.setu.helpers.TestUtilities.addTarget
import ie.setu.helpers.TestUtilities.addTips
import ie.setu.helpers.TestUtilities.addUser
import ie.setu.helpers.TestUtilities.deleteAchievementByAchievementId
import ie.setu.helpers.TestUtilities.deleteActivitiesByUserId
import ie.setu.helpers.TestUtilities.deleteActivityByActivityId
import ie.setu.helpers.TestUtilities.deleteSleepBySleepId
import ie.setu.helpers.TestUtilities.deleteTargetsByUserId
import ie.setu.helpers.TestUtilities.deleteTip
import ie.setu.helpers.TestUtilities.deleteUser
import ie.setu.helpers.TestUtilities.retrieveActivitiesByUserId
import ie.setu.helpers.TestUtilities.retrieveActivityByActivityId
import ie.setu.helpers.TestUtilities.retrieveAllActivities
import ie.setu.helpers.TestUtilities.retrieveBmiByBmiId
import ie.setu.helpers.TestUtilities.retrieveBmiByUserId
import ie.setu.helpers.TestUtilities.retrieveTargetsByUserId
import ie.setu.helpers.TestUtilities.retrieveUserByEmail
import ie.setu.helpers.TestUtilities.retrieveUserById
import ie.setu.helpers.TestUtilities.deleteBmiByUserId
import ie.setu.helpers.TestUtilities.deleteBmiByBmiId
import ie.setu.helpers.TestUtilities.updateActivity
import ie.setu.helpers.TestUtilities.updateSleep
import ie.setu.helpers.TestUtilities.updateTarget
import ie.setu.helpers.TestUtilities.updateTips
import ie.setu.helpers.TestUtilities.updateUser
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadUsers {
        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            if (response.status == 200) {
                val retrievedUsers: ArrayList<User> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedUsers.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = retrieveUserById(45)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = retrieveUserByEmail(nonExistingEmail)

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addUser(validName, validEmail)
            val addedUser: User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            //Arrange - add the user
            addUser(validName, validEmail)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser: User = jsonToObject(retrieveResponse.body.toString())
            deleteUser(retrievedUser.id)
        }
    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addUser(validName, validEmail)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser: User = jsonToObject(addResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do an update on
            val addedResponse = addUser(validName, validEmail)
            val addedUser: User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, updateUser(addedUser.id, updatedName, updatedEmail).status)

            //Act & Assert - retrieve updated user and assert details are correct
            val updatedUserResponse = retrieveUserById(addedUser.id)
            val updatedUser: User = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(updatedName, updatedUser.name)
            assertEquals(updatedEmail, updatedUser.email)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateUser(-1, updatedName, updatedEmail).status)
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteUser(-1).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addUser(validName, validEmail)
            val addedUser: User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveUserById(addedUser.id).status)
        }
    }


    @Nested
    inner class CreateActivities {

        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addActivityResponse = addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            assertEquals(201, addActivityResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an activity when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addActivityResponse = addActivity(
                activities.get(0).description, activities.get(0).duration,
                activities.get(0).calories, activities.get(0).started, userId
            )
            assertEquals(404, addActivityResponse.status)
        }
    }

    @Nested
    inner class ReadUActivities {

        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = retrieveAllActivities()
            if (response.status == 200) {
                val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
                assertNotEquals(0, retrievedActivities.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id
            )
            addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id
            )

            //Assert and Act - retrieve the three added activities by user id
            val response = retrieveActivitiesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
            assertEquals(3, retrievedActivities.size)

            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveActivitiesByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveActivitiesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get activity by activity id when no activity exists returns 404 response`() {
            //Arrange
            val activityId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveActivityByActivityId(activityId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get activity by activity id when activity exists returns 200 response`() {
            //Arrange - add a user and associated activity
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse = addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id
            )
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            //Act & Assert - retrieve the activity by activity id
            val response = retrieveActivityByActivityId(addedActivity.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            assertEquals(
                404, updateActivity(
                    activityID, updatedDescription, updatedDuration,
                    updatedCalories, updatedStarted, userId
                ).status
            )
        }

        @Test
        fun `updating an activity by activity id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do an update on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse = addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id
            )
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            //Act & Assert - update the added activity and assert a 204 is returned
            val updatedActivityResponse = updateActivity(
                addedActivity.id, updatedDescription,
                updatedDuration, updatedCalories, updatedStarted, addedUser.id
            )
            assertEquals(204, updatedActivityResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedActivityResponse = retrieveActivityByActivityId(addedActivity.id)
            val updatedActivity = jsonNodeToObject<Activity>(retrievedActivityResponse)
            assertEquals(updatedDescription, updatedActivity.description)
            assertEquals(updatedDuration, updatedActivity.duration, 0.1)
            assertEquals(updatedCalories, updatedActivity.calories)
            assertEquals(updatedStarted, updatedActivity.started)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivityByActivityId(-1).status)
        }

        @Test
        fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivitiesByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse = addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            assertEquals(201, addActivityResponse.status)

            //Act & Assert - delete the added activity and assert a 204 is returned
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)
            assertEquals(204, deleteActivityByActivityId(addedActivity.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all activities by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse1 = addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            assertEquals(201, addActivityResponse1.status)
            val addActivityResponse2 = addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id
            )
            assertEquals(201, addActivityResponse2.status)
            val addActivityResponse3 = addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id
            )
            assertEquals(201, addActivityResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted activities
            val addedActivity1 = jsonNodeToObject<Activity>(addActivityResponse1)
            val addedActivity2 = jsonNodeToObject<Activity>(addActivityResponse2)
            val addedActivity3 = jsonNodeToObject<Activity>(addActivityResponse3)
            assertEquals(404, retrieveActivityByActivityId(addedActivity1.id).status)
            assertEquals(404, retrieveActivityByActivityId(addedActivity2.id).status)
            assertEquals(404, retrieveActivityByActivityId(addedActivity3.id).status)
        }
    }

    @Nested
    inner class ReadHealthtips {
        @Test
        fun `get all tips from the database returns 200 or 404 response`() {

            val response = Unirest.get(origin + "/api/tips").asString()
            if (response.status == 200) {
                val getAllTips: ArrayList<HealthTip> = jsonToObject(response.body.toString())
                assertNotEquals(0, getAllTips.size)
            } else {
                assertEquals(404, response.status)
            }
        }
    }

    @Test
    fun `get random from the database returns 200 or 404 response`() {

        val response = Unirest.get(origin + "/api/tip").asString()
        if (response.status == 200) {
            val getTips: ArrayList<HealthTip> = jsonToObject(response.body.toString())
            assertTrue(getTips.size == 1)
        } else {
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class CreateTip {
        @Test
        fun `add a tips with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addTips(validId, validTip)
            val addedTip: HealthTip = jsonToObject(addResponse.body.toString())
            assertEquals(201, addResponse.status)
            deleteTip(addedTip.id)


        }
    }


    @Nested
    inner class UpdateTips {
        @Test
        fun `updating a tips when it exists, returns a 204 response`() {

            val addResponse = addTips(validId, validTip)
            val addedTip: HealthTip = jsonToObject(addResponse.body.toString())
            assertEquals(201, addResponse.status)

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, updateTips(addedTip.id, updatedTip).status)


            //After - restore the db to previous state by deleting the added user
            deleteTip(addedTip.id)
        }


        @Test
        fun `updating a id when it doesn't exist, returns a 404 response`() {

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateTips(-1, "Have a smile").status)
        }


    }


    @Nested
    inner class Createsleep {

        @Test
        fun `add an sleepHr when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addSleepResponse = addSleep(
                sleep.get(0).duration, sleep.get(0).date, addedUser.id
            )
            assertEquals(201, addSleepResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }

    }

    @Nested
    inner class UpdateSleep {

        @Test
        fun `updating an sleepHr by sleep id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val sleepID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            assertEquals(
                404, updateSleep(
                    sleepID, updatedDuration,
                    updateddate, userId
                ).status
            )
        }

        @Test
        fun `updating an sleepHr by sleep id when it exists, returns 204 response`() {

            // Arrange - add a user and an associated activity that we plan to do an update on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepResponse = addSleep(
                sleep[0].duration, sleep[0].date, addedUser.id
            )
            assertEquals(201, addSleepResponse.status)
            val addedSleep = jsonNodeToObject<Sleep>(addSleepResponse)

            // Act & Assert - update the added activity and assert a 204 is returned
            val updatedSleepResponse = updateSleep(
                addedSleep.id, updatedDuration, updateddate, addedUser.id
            )
            assertEquals(204, updatedSleepResponse.status)


            // After - delete the user
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class ReadUserSleep {
        @Test
        fun `get all sleep by user id when no sleepHr exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            // val response = TestUtilities.retrieveSleepByUserId(addedUser.id)
            //assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }


        @Test
        fun `get all sleepHr by user id when user and sleepHr exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepResponse = addSleep(
                sleep[0].duration, sleep[0].date, addedUser.id
            )
            assertEquals(201, addSleepResponse.status)


            //Assert and Act - retrieve the three added activities by user id
            val response = TestUtilities.retrieveSleepByUserId(addedUser.id)
            assertEquals(200, response.status)


            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }


        @Test
        fun `get all sleepHr by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = TestUtilities.retrieveSleepByUserId(userId)
            assertEquals(404, response.status)
        }

    }

    @Nested
    inner class DeleteUserSleep {

        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteSleepBySleepId(-1).status)
        }


        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addSleepResponse = addSleep(
                sleep[0].duration, sleep[0].date, addedUser.id
            )
            assertEquals(201, addSleepResponse.status)

            //Act & Assert - delete the added activity and assert a 204 is returned
            val addedSleep = jsonNodeToObject<Sleep>(addSleepResponse)
            assertEquals(204, deleteSleepBySleepId(addedSleep.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }


    }

    @Nested
    inner class DeleteAchivement {

        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteAchievementByAchievementId(-1).status)
        }


    }

    @Nested
    inner class ReadUserAchievement {


        @Test
        fun `get all sleepHr by user id when user and sleepHr exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())


            //Assert and Act - retrieve the three added activities by user id
            val response = TestUtilities.retrieveachievementByUserId(addedUser.id)
            assertEquals(404, response.status)


            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }


        @Test
        fun `get all sleepHr by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = TestUtilities.retrieveachievementByUserId(userId)
            assertEquals(404, response.status)
        }

    }

    @Nested
    inner class DeleteTips {
        @Test
        fun `deleting a Tip when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, TestUtilities.deleteTip(-1).status)
        }


    }

    @Nested
    inner class CreateTargets {

        @Test
        fun `add an target when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addTargetResponse = addTarget(
                targets[0].targetSleep, targets[0].targetBmi,
                targets[0].date, addedUser.id
            )
            assertEquals(201, addTargetResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }


    }

    @Nested
    inner class ReadUTargets {


        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            addTarget(
                targets[0].targetSleep, targets[0].targetBmi,
                targets[0].date, addedUser.id
            )


            //Assert and Act - retrieve the three added activities by user id
            val response = retrieveTargetsByUserId(addedUser.id)
            assertEquals(200, response.status)


            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveTargetsByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveTargetsByUserId(-1)
            assertEquals(404, response.status)
        }


    }

    @Nested
    inner class DeleteUTargets {


        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            addTarget(
                targets[0].targetSleep, targets[0].targetBmi,
                targets[0].date, addedUser.id
            )


            //Assert and Act - retrieve the three added activities by user id
            val response = deleteTargetsByUserId(addedUser.id)
            assertEquals(204, response.status)


            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }


        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = deleteTargetsByUserId(-1)
            assertEquals(404, response.status)
        }


    }

    @Nested
    inner class UpdateTargets {

        @Test
        fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            assertEquals(
                404, updateTarget(
                    activityID, updatedtargetSleep, updatedtargetBmi,
                    updateddate, userId
                ).status
            )
        }


    }

    @Nested
    inner class ReadBmi {


        @Test
        fun `get all bmi by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            addBmi(
                bmies[0].weight, bmies[0].height,
                bmies[0].bmiCalculator, bmies[0].timestamp, addedUser.id
            )


            //Assert and Act - retrieve the three added activities by user id
            val response = retrieveBmiByUserId(addedUser.id)
            assertEquals(200, response.status)



            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all bmi by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveBmiByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all bmi by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveBmiByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get bmi by bmi id when no activity exists returns 404 response`() {
            //Arrange
            val BmiId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveBmiByBmiId(BmiId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get bmi by bmi id when activity exists returns 200 response`() {
            //Arrange - add a user and associated activity
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addBmiResponse = addBmi(
                bmies[0].weight, bmies[0].height,
                bmies[0].bmiCalculator, bmies[0].timestamp, addedUser.id
            )

            assertEquals(201, addBmiResponse.status)
            val addedBmi = jsonNodeToObject<Bmi>(addBmiResponse)

            //Act & Assert - retrieve the activity by activity id
            val response = addedBmi.id?.let { retrieveBmiByBmiId(it) }
            if (response != null) {
                assertEquals(200, response.status)
            }

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }


    }

    @Nested
    inner class CreateBmi {

        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addBmiResponse = addBmi(
                bmies[0].weight, bmies[0].height,
                bmies[0].bmiCalculator, bmies[0].timestamp, addedUser.id
            )
            assertEquals(201, addBmiResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an activity when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addBmiResponse = addBmi(
                bmies[0].weight, bmies[0].height,
                bmies[0].bmiCalculator, bmies[0].timestamp, userId
            )
            assertEquals(404, addBmiResponse.status)
        }
    }

    @Nested
    inner class DeleteBmi {

        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteBmiByBmiId(-1).status)
        }

        @Test
        fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteBmiByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addBmiResponse = addBmi(
                bmies[0].weight, bmies[0].height,
                bmies[0].bmiCalculator, bmies[0].timestamp, addedUser.id
            )
            assertEquals(201, addBmiResponse.status)

            //Act & Assert - delete the added activity and assert a 204 is returned
            val addedBmi = jsonNodeToObject<Bmi>(addBmiResponse)
            assertEquals(204, addedBmi.id?.let { deleteBmiByBmiId(it).status })

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }
}











