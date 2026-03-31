import factory.*
import repository.*
import service.*
import model.params.*
import model.enum.CurrencyType
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    try {
        println("=== Запуск Менеджера особистих фінансів (CLI) ===")

        // 1. Ініціалізація фабрик
        val userFactory = UserFactory()
        val accountFactory = AccountFactory()
        val transactionFactory = TransactionFactory()

        // 2. Ініціалізація репозиторіїв (вказуємо шляхи до JSON файлів)
        val userRepository = UserRepository("users.json")
        val accountRepository = AccountRepository("accounts.json")
        val transactionRepository = TransactionRepository("transactions.json")
        val categoryRepository = CategoryRepository("categories.json")

        // 3. Асинхронне завантаження даних (те, що ми змінили замість init)
        println("Завантаження даних з файлів...")
        userRepository.loadData()
        accountRepository.loadData()
        transactionRepository.loadData()
        categoryRepository.loadData()

        // 4. Ініціалізація сервісів
        val currencyExchange = BasicCurrencyExchangeService()
        val userService = UserService(userRepository, userFactory)
        val accountService = AccountService(accountRepository, accountFactory)
        val transactionService = TransactionService(transactionRepository, accountService, transactionFactory, currencyExchange)
        val categoryService = CategoryService(categoryRepository)

        // --- ДЕМОНСТРАЦІЯ ЛОГІКИ ПРОТОТИПУ ---

        println("\n--- 1. Реєстрація та Авторизація ---")
        val registerSuccess = userService.register(UserCreationParams("admin", "12345"))
        if (registerSuccess) println("Користувача 'admin' успішно зареєстровано!")

        val currentUser = userService.login("admin", "12345")
        if (currentUser == null) {
            println("Помилка авторизації!")
            return@runBlocking // Виходимо, якщо логін не вдався
        }
        println("Авторизовано як: ${currentUser.login}")

        println("\n--- 2. Створення категорії ---")
        val foodCategory = categoryService.createCategory("Їжа та напої")
        val salaryCategory = categoryService.createCategory("Зарплата")
        println("Створено категорії: ${foodCategory?.name}, ${salaryCategory?.name}")

        println("\n--- 3. Створення рахунку ---")
        // Створюємо гаманець
        val cashAccountParams = AccountCreationParams.Cash(
            userId = currentUser.id,
            initBalance = 0L,
            currency = CurrencyType.UAH,
            cashLocation = "Кишеня",
            dailyLimit = 500L
        )
        accountService.createAccount(cashAccountParams)

        // Отримуємо створений рахунок (для прототипу беремо перший ліпший)
        val userAccounts = accountRepository.getAll().filter { it.userId == currentUser.id }
        val myWallet = userAccounts.firstOrNull()

        if (myWallet != null) {
            println("Створено рахунок: ${myWallet.accountType} з балансом ${myWallet.balance} ${myWallet.currency}")

            println("\n--- 4. Проведення транзакцій ---")

            // Дохід
            transactionService.income(
                TransactionCreationParams.Income(
                    userId = currentUser.id,
                    accountId = myWallet.id,
                    amount = 15000L,
                    currency = CurrencyType.UAH,
                    date = "2026-03-30T10:00:00",
                    categoryId = salaryCategory?.id ?: "",
                    note = "Аванс"
                )
            )
            println("Додано дохід 15000 UAH. Поточний баланс: ${accountService.getConcreteBalance(myWallet.id)}")

            // Витрата
            transactionService.expense(
                TransactionCreationParams.Expense(
                    userId = currentUser.id,
                    accountId = myWallet.id,
                    amount = 250L,
                    currency = CurrencyType.UAH,
                    date = "2026-03-30T13:00:00",
                    categoryId = foodCategory?.id ?: "",
                    note = "Обід в кафе"
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