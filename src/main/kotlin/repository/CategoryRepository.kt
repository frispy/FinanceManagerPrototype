package repository

import kotlinx.serialization.serializer
import model.transaction.TransactionCategory

class CategoryRepository(path: String) : BaseRepository<TransactionCategory>(
    path,
    serializer<List<TransactionCategory>>()
) {
    fun findByName(name: String): TransactionCategory? {
        return itemsFlow.value.find { it.name == name }
    }
}