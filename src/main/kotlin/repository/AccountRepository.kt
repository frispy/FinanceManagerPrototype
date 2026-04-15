package repository

import data.dao.AccountDao
import data.entity.toDomain
import data.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.account.Account

class AccountRepository(private val dao: AccountDao) {

    fun getAllAccountsFlow(): Flow<List<Account>> {
        return dao.getAllFlow().map { list -> list.map { it.toDomain() } }
    }

    suspend fun getById(id: String): Account? {
        return dao.getById(id)?.toDomain()
    }

    suspend fun add(account: Account) {
        dao.insert(account.toEntity())
    }

    suspend fun update(account: Account) {
        dao.update(account.toEntity())
    }

    suspend fun delete(id: String) {
        dao.delete(id)
    }
}