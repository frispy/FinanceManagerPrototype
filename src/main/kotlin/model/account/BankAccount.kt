package model.account

import model.enum.AccountType
import model.enum.CurrencyType
import kotlinx.serialization.Serializable

@Serializable
data class BankAccount(
    override val userId: String,
    override var balance: Long = 0,
    override val currency: CurrencyType,
    val bankName: String,
    override val id: String
) : Account() {
    override val type: AccountType = AccountType.BANK
}