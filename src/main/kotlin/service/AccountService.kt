package service

import factory.GenericFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.account.Account
import model.enum.CurrencyType
import model.params.AccountCreationParams
import repository.UnitOfWork

class AccountService (
    private val unitOfWork: UnitOfWork,
    private val accountFactory: GenericFactory<Account, AccountCreationParams>,
) {
    suspend fun createAccount(params: AccountCreationParams): Boolean {
        val account = accountFactory.create(params)
        unitOfWork.accountRepository.add(account)
        return true
    }

    suspend fun getAccountCurrency(accountId: String): CurrencyType? {
        return unitOfWork.accountRepository.getById(accountId)?.currency
    }

    // remove money
    suspend fun withdraw(accountId: String, amount: Long): Boolean {
        val account = unitOfWork.accountRepository.getById(accountId) ?: return false

        if (amount <= 0 || account.balance < amount) {
            return false
        }

        account.updateBalance(account.balance - amount)
        unitOfWork.accountRepository.update(account)
        return true
    }

    // add money
    suspend fun deposit(accountId: String, amount: Long): Boolean {
        val account = unitOfWork.accountRepository.getById(accountId) ?: return false

        if (amount <= 0) {
            return false
        }

        account.updateBalance(account.balance + amount)
        unitOfWork.accountRepository.update(account)
        return true
    }

    suspend fun deleteAccount(accountId: String) {
        unitOfWork.accountRepository.delete(accountId)
    }

    fun getTotalBalanceFlow(userId: String): Flow<Long> {
        return unitOfWork.accountRepository.getAllAccountsFlow().map { accountsList ->
            // get total sum of accounts
            accountsList.filter { it.userId == userId }.sumOf { it.balance }
        }
    }

    suspend fun getConcreteBalance(accountId: String): String {
        return unitOfWork.accountRepository.getById(accountId)?.balance?.toString() ?: "0"
    }
}