package service

import model.transaction.TransactionCategory
import repository.CategoryRepository
import repository.UnitOfWork
import java.util.UUID

class CategoryService(
    private val unitOfWork: UnitOfWork,
) {
    suspend fun createCategory(name: String): TransactionCategory? {
        // check for duplicates
        val existing = unitOfWork.categoryRepository.findByName(name)
        if (existing != null) {
            return null
        }

        // generate new id and object
        val newCategory = TransactionCategory(
            id = UUID.randomUUID().toString(),
            name = name
        )

        // save to file
        unitOfWork.categoryRepository.add(newCategory)
        return newCategory
    }

    // fetch all stored categories
    fun getAllCategories(): List<TransactionCategory> {
        return unitOfWork.categoryRepository.getAll()
    }

    // delete specific category
    suspend fun deleteCategory(categoryId: String) {
        unitOfWork.categoryRepository.delete(categoryId)
    }
}