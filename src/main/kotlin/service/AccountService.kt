package service

import factory.GenericFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.account.Account
import model.enum.CurrencyType
import model.params.AccountCreationParams
import repository.AccountRepository

class AccountService (
    private val accountRepository: AccountRepository,
    private val accountFactory: GenericFactory<Account, AccountCreationParams>,
) {
    suspend fun createAccount(params: AccountCreationParams): Boolean {
        val account = accountFactory.create(params)
        accountRepository.add(account)
        return true
    }

    fun getAccountCurrency(accountId: String): CurrencyType? {
        return accountRepository.getById(accountId)?.currency
    }

    // remove money
    suspend fun withdraw(accountId: String, amount: Long): Boolean {
        val account = accountRepository.getById(accountId) ?: return false

        if (amount <= 0 || account.balance < amount) {
            return false
        }

        account.updateBalance(account.balance - amount)
        accountRepository.update(account)

        return true
    }

    // add money
    suspend fun deposit(accountId: String, amount: Long): Boolean {
        val account = accountRepository.getById(accountId) ?: return false

        if (amount <= 0) {
            return false
        }

        account.updateBalance(account.balance + amount)
        accountRepository.update(account)

        return true
    }

    suspend fun deleteAccount(accountId: String) {
        accountRepository.delete(accountId)
    }

//    fun getTotalBalance(userId: String): String {
//        val accounts = accountRepository.findByUserId(userId)
//        return accounts.sumOf { it.balance }.toString()
//    }

    fun getTotalBalanceFlow(userId: String): Flow<Long> {
        // take the flow of account lists from repository
        return accountRepository.itemsFlow.map { accountsList ->
            // on each account update, filter it and count the sum
            accountsList.filter { it.userId == userId }.sumOf { it.balance }
        }
    }

    fun getConcreteBalance(accountId: String): String {
        return accountRepository.getById(accountId)?.balance.toString()
    }
}