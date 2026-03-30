package service

import factory.GenericFactory
import model.account.Account
import model.enum.CurrencyType
import model.params.AccountCreationParams
import repository.AccountRepository

class AccountService (
    private val accountRepository: AccountRepository,
    private val accountFactory: GenericFactory<Account, AccountCreationParams>,
) {
    fun createAccount(params: AccountCreationParams): Boolean {
        val account = accountFactory.create(params)
        accountRepository.add(account)
        return true
    }

    fun getAccountCurrency(accountId: String): CurrencyType? {
        return accountRepository.getById(accountId)?.currency
    }

    // remove money
    fun withdraw(accountId: String, amount: Long): Boolean {
        val account = accountRepository.getById(accountId) ?: return false

        if (amount <= 0 || account.balance < amount) {
            return false
        }

        account.balance -= amount
        accountRepository.update(account)

        return true
    }

    // add money
    fun deposit(accountId: String, amount: Long): Boolean {
        val account = accountRepository.getById(accountId) ?: return false

        if (amount <= 0) {
            return false
        }

        account.balance += amount
        accountRepository.update(account)

        return true
    }

    fun deleteAccount(accountId: String) {
        accountRepository.delete(accountId)
    }

    fun getTotalBalance(userId: String): String {
        val accounts = accountRepository.findByUserId(userId)
        return accounts.sumOf { it.balance }.toString()
    }

    fun getConcreteBalance(accountId: String): String {
        return accountRepository.getById(accountId)?.balance.toString()
    }
}