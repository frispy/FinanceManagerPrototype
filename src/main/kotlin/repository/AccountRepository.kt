package repository

import kotlinx.serialization.serializer
import model.account.Account

class AccountRepository(path: String) : BaseRepository<Account>(
    path,
    serializer()
) {
    fun findByUserId(userId: String): List<Account> {
        return itemsFlow.value.filter { it.userId == userId } // get all accounts
    }
}