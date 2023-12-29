package ie.setu.controllers

import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 *  Controller for handling user-related operations
*/

object UserController {

    private val userDao = UserDAO()
/**
    * Retrieve all users and return JSON response
    */
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(users)
    }
    /**
     *  Retrieve user by user ID and return JSON response
     */
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }
    /**
     *  Add a new user and return JSON response with updated ID
     */

    fun addUser(ctx: Context) {
        var user: User = jsonToObject(ctx.body())
        val userId = userDao.save(user)
        if (userId != null) {
            user = user.copy(id = userId) // Use the copy function to create a new user with the updated id
            ctx.json(user)
            ctx.status(201)
        }
    }
    /**
     *  Retrieve user by email and return JSON response
     */

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }
    /**
     *  Retrieve user by email and return JSON response
     */
    fun deleteUser(ctx: Context){
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    /**
     *  Update user information and return appropriate HTTP status code
     */
    fun updateUser(ctx: Context){
        val foundUser : User = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), user=foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}



