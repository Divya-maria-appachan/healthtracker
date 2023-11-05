package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Bmi
import ie.setu.domain.HealthTip
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Bmies
import ie.setu.domain.db.HealthTips
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.BmiDAO
import ie.setu.domain.repository.HealthTipDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"





val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 500
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")





val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)


val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "yoga", duration = 60.0, calories = 100, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Dancing", duration = 30.0, calories = 220, started = DateTime.now(), userId = 2)
)
val healthTips = arrayListOf<HealthTip>(
    HealthTip(id = 1, tips = "Take breaks and relax during work."),
    HealthTip(id = 2, tips = "Connect with friends and family."),
    HealthTip(id = 3, tips = "Limit sugar and processed foods."),
    HealthTip(id = 4, tips = "Exercise regularly.")
)
val bmies = arrayListOf<Bmi>(
    Bmi(id = 1, userId = 1,  weight = 60.0, height = 168.0, bmiCalculator = "21.258503401360546", timestamp=DateTime.now()),
    Bmi(id = 2, userId = 2,  weight = 59.0, height = 154.0, bmiCalculator = "24.87771968291449", timestamp=DateTime.now()),
    Bmi(id = 3, userId = 3,  weight = 58.0, height = 150.0, bmiCalculator = "25.77777777777778", timestamp=DateTime.now())
    )



fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}
fun populatehealthTipsTable(): HealthTipDAO {
    SchemaUtils.create(HealthTips)
    val healthTipDAO = HealthTipDAO()
    healthTipDAO.addHealthTip(healthTips[0])
    healthTipDAO.addHealthTip(healthTips[1])
    healthTipDAO.addHealthTip(healthTips[2])
    return healthTipDAO
}
fun populatebmisTable(): BmiDAO {
    SchemaUtils.create(Bmies)
    val bmiDAO = BmiDAO()
    bmiDAO .save(bmies[0])
    bmiDAO .save(bmies[1])
    bmiDAO .save(bmies[2])
    return bmiDAO
}

