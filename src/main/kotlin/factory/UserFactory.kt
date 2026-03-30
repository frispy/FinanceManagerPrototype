package factory

import model.user.User
import model.params.UserCreationParams
import java.util.UUID

class UserFactory : GenericFactory<User, UserCreationParams> {
    override fun create(params: UserCreationParams): User {
        val userId = UUID.randomUUID().toString()
        val passwordHash = params.pass.toByteArray()
        return User(userId, params.login, passwordHash)
    }
}