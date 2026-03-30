package model.params

import model.enum.CurrencyType

sealed class TransactionCreationParams {
    abstract val userId: String
    abstract val accountId: String
    abstract val amount: Long
    abstract val currency: CurrencyType
    abstract val date: String
    abstract val categoryId: String
    abstract val note: String

    data class Expense(
        override val userId: String,
        override val accountId: String,
        override val amount: Long,
        override val currency: CurrencyType,
        override val date: String,
        override val categoryId: String,
        override val note: String
    ) : TransactionCreationParams()

    data class Income(
        override val userId: String,
        override val accountId: String,
        override val amount: Long,
        override val currency: CurrencyType,
        override val date: String,
        override val categoryId: String,
        override val note: String
    ) : TransactionCreationParams()

    data class Transfer(
        override val userId: String,
        override val accountId: String,
        val targetAccountId: String,
        override val amount: Long,
        override val currency: CurrencyType,
        override val date: String,
        override val categoryId: String,
        override val note: String
    ) : TransactionCreationParams()
}