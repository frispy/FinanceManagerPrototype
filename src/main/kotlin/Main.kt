import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import factory.*
import repository.*
import service.*
import model.params.*
import model.enum.CurrencyType
import data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    try {
        println("=== Запуск Менеджера особистих фінансів (CLI) ===")

        // 1. Ініціалізація фабрик
        val userFactory = UserFactory()
        val accountFactory = AccountFactory()
        val transactionFactory = TransactionFactory()

        // 2. Ініціалізація Бази Даних (Room)
        val database = createTestDatabase()
        println("Підключення до бази даних успішне...")

        // 3. Ініціалізація Репозиторіїв (замість UnitOfWork)
        val userRepository: UserRepository = UserRepositoryImpl(database.userDao())
        val accountRepository: AccountRepository = AccountRepositoryImpl(database.accountDao())
        val transactionRepository: TransactionRepository = TransactionRepositoryImpl(database.transactionDao())
        val categoryRepository: CategoryRepository = CategoryRepositoryImpl(database.categoryDao())

        // 4. Ініціалізація сервісів (впровадження залежностей через конструктор)
        val currencyExchange = BasicCurrencyExchangeService()

        val userService = UserService(userRepository, userFactory)
        val accountService = AccountService(accountRepository, accountFactory)
        val transactionService = TransactionService(
            transactionRepository = transactionRepository,
            accountService = accountService,
            transactionFactory = transactionFactory,
            currencyExchange = currencyExchange
        )
        val categoryService = CategoryService(categoryRepository)

        // --- ДЕМОНСТРАЦІЯ ЛОГІКИ ---

        println("\n--- 1. Реєстрація ---")
        userService.register(UserCreationParams("admin", "12345"))
        val currentUser = userService.login("admin", "12345") ?: return@runBlocking

        println("\n--- 2. Створення рахунку ---")
        val cashAccountParams = AccountCreationParams.Cash(
            userId = currentUser.id,
            initBalance = 0L,
            currency = CurrencyType.UAH,
            cashLocation = "Кишеня",
            dailyLimit = 500L
        )
        accountService.createAccount(cashAccountParams)

        println("\n--- 3. Створення категорії ---")
        val foodCategory = categoryService.createCategory("Їжа та напої")
        val salaryCategory = categoryService.createCategory("Зарплата")
        println("Створено категорії: ${foodCategory?.name}, ${salaryCategory?.name}")

        // Звертаємось напряму до потрібного репозиторію
        val allAccounts = accountRepository.getAllAccountsFlow().first()
        val myWallet = allAccounts.firstOrNull { it.userId == currentUser.id }

        if (myWallet != null) {
            println("Рахунок готовий. Баланс: ${accountService.getConcreteBalance(myWallet.id)}")

            println("\n--- 4. Проведення транзакцій ---")

            // Дохід
            transactionService.income(
                TransactionCreationParams.Income(
                    common = TransactionCreationParams.Common(
                        userId = currentUser.id,
                        accountId = myWallet.id,
                        amount = 15000L,
                        currency = CurrencyType.UAH,
                        date = "2026-03-30T10:00:00",
                        categoryId = salaryCategory?.id ?: "",
                        note = "Аванс"
                    )
                )
            )
            println("Додано дохід 15000 UAH. Поточний баланс: ${accountService.getConcreteBalance(myWallet.id)}")

            // Витрата
            transactionService.expense(
                TransactionCreationParams.Expense(
                    TransactionCreationParams.Common(
                        userId = currentUser.id,
                        accountId = myWallet.id,
                        amount = 250L,
                        currency = CurrencyType.UAH,
                        date = "2026-03-30T13:00:00",
                        categoryId = foodCategory?.id ?: "",
                        note = "Обід в кафе"
                    )
                )
            )
            println("Проведено витрату 250 UAH. Поточний баланс: ${accountService.getConcreteBalance(myWallet.id)}")
        }

        println("\n=== Завершення роботи ===")
    } catch (e: Exception) {
        println("\n❌ КРИТИЧНА ПОМИЛКА ПІД ЧАС ВИКОНАННЯ:")
        e.printStackTrace()
    }
}

fun createTestDatabase(): AppDatabase {
    val dbFile = java.io.File("my_finances.db")

    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}