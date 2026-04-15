package repository

import data.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import model.transaction.TransactionCategory

class CategoryRepository(private val dao: CategoryDao) {

    fun getAllCategoriesFlow(): Flow<List<TransactionCategory>> {
        return dao.getAllFlow()
    }

    suspend fun getById(id: String): TransactionCategory? {
        return dao.getById(id)
    }

    suspend fun findByName(name: String): TransactionCategory? {
        return dao.findByName(name)
    }

    suspend fun add(category: TransactionCategory) {
        dao.insert(category)
    }

    suspend fun update(category: TransactionCategory) {
        dao.update(category)
    }

    suspend fun delete(id: String) {
        dao.delete(id)
    }
}