package repository

import data.AppDatabase

class UnitOfWork(private val database: AppDatabase) {
    val accountRepository: AccountRepository by lazy {
        AccountRepository(database.accountDao())
    }

    val userRepository: UserRepository by lazy {
        UserRepository(database.userDao())
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao())
    }
}