package repository

class UnitOfWork(private val baseDirectory: String) {
    val accountRepository: AccountRepository by lazy {
        AccountRepository("$baseDirectory/accounts.json")
    }

    val userRepository: UserRepository by lazy {
        UserRepository("$baseDirectory/users.json")
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository("$baseDirectory/transactions.json")
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository("$baseDirectory/categories.json")
    }
}