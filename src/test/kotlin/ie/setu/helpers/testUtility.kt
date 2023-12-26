package ie.setu.helpers

import ie.setu.domain.db.userSleep.id
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime

object TestUtilities {


    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    //helper function to add a test user to the database
    fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id").asString()
    }

    //helper function to retrieve a test user from the database by email
    fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    //helper function to retrieve a test user from the database by id
    fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    //helper function to add a test user to the database
    fun updateUser (id: Int, name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to retrieve all activities
    fun retrieveAllActivities(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/activities").asJson()
    }

    //helper function to retrieve activities by user id
    fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/activities").asJson()
    }

    //helper function to retrieve activity by activity id
    fun retrieveActivityByActivityId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities/${id}").asJson()
    }

    //helper function to delete an activity by activity id
    fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/activities/$id").asString()
    }

    //helper function to delete an activity by activity id
    fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id/activities").asString()
    }

    //helper function to add a test user to the database
    fun updateActivity(id: Int, description: String, duration: Double, calories: Int,
                       started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/activities/$id")
            .body("""
                {
                  "description":"$description",
                  "duration":$duration,
                  "calories":$calories,
                  "started":"$started",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    //helper function to add an activity
    fun addActivity(description: String, duration: Double, calories: Int,
                    started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/activities")
            .body("""
                {
                   "description":"$description",
                   "duration":$duration,
                   "calories":$calories,
                   "started":"$started",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }
    fun addTips (id: Int, tips: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/tip")
            .body("{\"id\":\"$id\", \"tips\":\"$tips\"}")
            .asJson()
    }
    fun getTips() : HttpResponse<String> {
        return Unirest.get(origin + "/api/tip").asString()
    }
    fun getAllTips(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/tips").asJson()
    }
    fun updateTips(id: Int, tips: String): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/tip/$id")
            .body("{\"id\":\"$id\", \"tips\":\"$tips\"}")
            .asJson()
    }
    fun deleteTip(id: Int): HttpResponse<JsonNode> {
        return Unirest.delete("$origin/api/tip/$id").asJson()
    }
    fun retrieveSleepByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/users/${id}/sleep").asJson()
    }


    fun addSleep(duration: Double, date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users/${id}/sleep")
            .body("""
                {
                  
                   "duration":$duration,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }


    fun updateSleep(id: Int, duration: Double, date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/sleep/$id")
            .body("""
                {
                   "id":$id,
                   "duration":$duration,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    fun deleteSleepBySleepId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/sleep/$id").asString()
    }

    fun deleteAchievementByAchievementId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/achievement/$id").asString()
    }
    fun retrieveachievementByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/users/${id}/achievement").asJson()
    }

}