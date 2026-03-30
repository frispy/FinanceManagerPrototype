package model.params

import model.enum.CurrencyType

sealed class AccountCreationParams {
    abstract val userId: String
    abstract val initBalance: Long
    abstract val currency: CurrencyType

    data class Bank(
        override val userId: String,
        override val initBalance: Long,
        override val currency: CurrencyType,
        val bankName: String
    ) : AccountCreationParams()

    data class Cash(
        override val userId: String,
        override val initBalance: Long,
        override val currency: CurrencyType,
        val cashLocation: String,
        val dailyLimit: Long = 0
    ) : AccountCreationParams()

    data class Deposit(
        override val userId: String,
        override val initBalance: Long,
        override val currency: CurrencyType,
        val interestRate: Double
    ) : AccountCreationParams()
}