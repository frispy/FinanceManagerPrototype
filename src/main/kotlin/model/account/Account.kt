package model.account

import model.enum.AccountType
import model.enum.CurrencyType
import kotlinx.serialization.Serializable
import model.Identifiable

@Serializable
sealed class Account : Identifiable {
    abstract val userId: String
    abstract var balance: Long
    abstract val currency: CurrencyType
    abstract val type: AccountType
    override abstract val id: String
}