package model.account

import model.enum.AccountType
import model.enum.CurrencyType
import kotlinx.serialization.Serializable
import model.transaction.Transaction

@Serializable
data class DepositAccount(
    override val userId: String,
    override var balance: Long = 0,
    override val currency: CurrencyType,
    val interestRate: Double,
    override val id: String
) : Account() {
    override val type: AccountType = AccountType.DEPOSIT
}