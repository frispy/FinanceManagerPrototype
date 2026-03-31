package repository

import kotlinx.serialization.serializer
import model.transaction.Transaction
import model.user.User

class TransactionRepository(path: String) : BaseRepository<Transaction>(
    path,
    serializer()
) {
    fun getTransactionsByAccount(accountId: String): List<Transaction> {
        return itemsFlow.value.filter { it.accountId == accountId }
    }
}