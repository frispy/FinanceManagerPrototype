package model.account

import model.enum.AccountType
import model.enum.CurrencyType
import kotlinx.serialization.Serializable

@Serializable
data class CashAccount(
    override val userId: String,
    override var balance: Long = 0,
    override val currency: CurrencyType,
    val cashLocation: String,
    val dailyLimit: Long,
    override val id: String
) : Account() {
    override val type: AccountType = AccountType.CASH
}