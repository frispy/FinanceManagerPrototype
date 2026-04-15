package repository

import data.dao.UserDao
import model.user.User

class UserRepository(private val dao: UserDao) {
    suspend fun findByLogin(login: String): User? = dao.findByLogin(login)
    suspend fun add(user: User) = dao.insert(user)
    suspend fun update(user: User) = dao.update(user)
}