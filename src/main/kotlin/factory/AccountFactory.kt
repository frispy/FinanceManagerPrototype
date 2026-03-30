package factory

import model.account.Account
import model.account.BankAccount
import model.account.CashAccount
import model.account.DepositAccount
import model.params.AccountCreationParams
import java.util.UUID

class AccountFactory : GenericFactory<Account, AccountCreationParams> {
    override fun create(params: AccountCreationParams): Account {
        val accountId = UUID.randomUUID().toString()

        return when (params) {
            is AccountCreationParams.Bank -> {
                BankAccount(params.userId, params.initBalance, params.currency, params.bankName, accountId)
            }
            is AccountCreationParams.Cash -> {
                CashAccount(params.userId, params.initBalance, params.currency, params.cashLocation, params.dailyLimit, accountId)
            }
            is AccountCreationParams.Deposit -> {
                DepositAccount(params.userId, params.initBalance, params.currency, params.interestRate, accountId)
            }
        }
    }
}