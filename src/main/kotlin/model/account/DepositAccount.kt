package model.account

import model.enum.AccountType
import model.enum.CurrencyType
import kotlinx.serialization.Serializable
import model.transaction.Transaction

@Serializable
data class DepositAccount(
    override val userId: String,
    override val balance: Long = 0,
    override val currency: CurrencyType,
    val interestRate: Double,
    override val id: String
) : Account() {
    override val accountType: AccountType = AccountType.DEPOSIT

    override fun updateBalance(newBalance: Long): Account {
        return this.copy(balance = newBalance)
    }
}