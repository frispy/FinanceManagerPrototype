package service

import model.transaction.TransactionCategory
import repository.CategoryRepository
import java.util.UUID

class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    suspend fun createCategory(name: String): TransactionCategory? {
        // check for duplicates
        val existing = categoryRepository.findByName(name)
        if (existing != null) {
            return null
        }

        // generate new id and object
        val newCategory = TransactionCategory(
            id = UUID.randomUUID().toString(),
            name = name
        )

        // save to file
        categoryRepository.add(newCategory)
        return newCategory
    }

    // fetch all stored categories
    fun getAllCategories(): List<TransactionCategory> {
        return categoryRepository.getAll()
    }

    // delete specific category
    suspend fun deleteCategory(categoryId: String) {
        categoryRepository.delete(categoryId)
    }
}