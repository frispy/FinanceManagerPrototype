package model.transaction

import kotlinx.serialization.Serializable
import model.enum.CurrencyType
import model.Identifiable
import model.enum.TransactionType

@Serializable
sealed class Transaction : Identifiable {
    abstract override val id: String
    abstract val userId: String
    abstract val accountId: String
    abstract val amount: Long
    abstract val currency: CurrencyType
    abstract val date: String
    abstract val categoryId: String
    abstract val note: String
    abstract val transactionType: TransactionType

    @Serializable
    data class Expense(
        override val id: String,
        override val userId: String,
        override val accountId: String,
        override val amount: Long,
        override val currency: CurrencyType,
        override val date: String,
        override val categoryId: String,
        override val note: String,
        override val transactionType: TransactionType = TransactionType.EXPENSE,
    ) : Transaction()

    @Serializable
    data class Income(
        override val id: String,
        override val userId: String,
        override val accountId: String,
        override val amount: Long,
        override val currency: CurrencyType,
        override val date: String,
        override val categoryId: String,
        override val note: String,
        override val transactionType: TransactionType = TransactionType.INCOME,
    ) : Transaction()

    @Serializable
    data class Transfer(
        override val id: String,
        override val userId: String,
        override val accountId: String,
        val targetAccountId: String, // mandatory for transfers
        override val amount: Long,
        override val currency: CurrencyType,
        override val date: String,
        override val categoryId: String,
        override val note: String,
        override val transactionType: TransactionType = TransactionType.TRANSFER,
    ) : Transaction()
}