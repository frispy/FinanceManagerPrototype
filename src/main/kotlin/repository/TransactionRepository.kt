package repository

import data.dao.TransactionDao
import data.entity.toDomain
import data.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.transaction.Transaction

class TransactionRepository(private val dao: TransactionDao) {

    fun getAllTransactionsFlow(): Flow<List<Transaction>> {
        return dao.getAllFlow().map { list -> list.map { it.toDomain() } }
    }

    suspend fun getById(id: String): Transaction? {
        return dao.getById(id)?.toDomain()
    }

    suspend fun getTransactionsByAccount(accountId: String): List<Transaction> {
        return dao.getByAccountId(accountId).map { it.toDomain() }
    }

    // variant for flow
    fun getTransactionsByAccountFlow(accountId: String): Flow<List<Transaction>> {
        return dao.getByAccountIdFlow(accountId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun add(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    suspend fun update(transaction: Transaction) {
        dao.update(transaction.toEntity())
    }

    suspend fun delete(id: String) {
        dao.delete(id)
    }
}