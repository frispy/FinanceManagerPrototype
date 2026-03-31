package repository

import kotlinx.serialization.serializer
import model.user.User

class UserRepository(path: String) : BaseRepository<User>(
    path,
    serializer<List<User>>()
) {
    fun findByLogin(login: String): User? {
        return itemsFlow.value.find { it.login == login }
    }
}