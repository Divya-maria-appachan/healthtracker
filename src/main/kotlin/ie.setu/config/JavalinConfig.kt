package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 : Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("bmi") {
                        get(BMIController::getBmiByUserId)
                        delete(BMIController::deleteBmiByUserId)

                    }
                    path("activities") {
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                    path("sleep") {
                        post(SleepController::addSleep)
                        get(SleepController::getSleepByUserId)
                    }
                    path("achievements"){

                        get(AchievementController::getAchievementsByUserId)
                    }
                    path("targets"){
                        post(TargetController::addTarget)
                        get(TargetController::getTargetsByUserId)
                        delete(TargetController::deleteTargetsByUserId)
                        patch(TargetController::updateTargetsByUserId)
                    }
                }

                path("/email/{email}") {
                    get(UserController::getUserByEmail)

                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)

                path("{activity-id}") {
                    get(ActivityController::getActivityById)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }
            path("/api/bmi"){
                get(BMIController::getAllBmi)
                post(BMIController::calculateBmi)

                path("{bmi-id}"){
                    get(BMIController::getByBmiId)
                    delete(BMIController::deleteBmiId)
                }



            }
            path("/api/tip"){
                post(HealthTipController::addTips)
                get(HealthTipController::getTips)
                path("{tip-id}"){
                    patch(HealthTipController::updateTips)
                    delete(HealthTipController::deleteTip)
                }
            }
            path("/api/tips"){
                get(HealthTipController::getAllTips)

            }
            path("/api/sleep/{id}") {
                patch(SleepController::updateSleepById)
                delete(SleepController::deleteSleepById)
            }
            path("/api/achievement/{id}") {
                delete(AchievementController::deleteByAchievementId)

            }

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/users/{user-id}/bmi", VueComponent("<user-bmi-overview></user-bmi-overview>"))
            get("/users/{user-id}/sleep", VueComponent("<user-sleep></user-sleep>"))
            get("/tip", VueComponent("<tips-overview></tips-overview>"))
            get("/users/{user-id}/targets", VueComponent("<user-targets></user-targets>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }
}
