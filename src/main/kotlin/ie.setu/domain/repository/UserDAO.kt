package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    fun findById(id: Int): User? {
        var user: User? = null
        transaction {
            Users.select { Users.id eq id }.singleOrNull()?.let {
                user = mapToUser(it)
            }
        }
        return user
    }


    fun save(user: User){
        transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }

    fun findByEmail(email: String): User? {
        var user: User? = null
        transaction {
            Users.select { Users.email eq email }.singleOrNull()?.let {
                user = mapToUser(it)
            }
        }
        return user

    }


    fun delete(id: Int):Int{
        return transaction{
            Users.deleteWhere{ Users.id eq id
        }
        }
    }

    fun update(id: Int, user: User){
        transaction {
            Users.update ({
                Users.id eq id}) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }

}
