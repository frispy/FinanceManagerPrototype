package factory

import model.params.TransactionCreationParams
import model.transaction.Transaction
import model.transaction.TransactionBase
import java.time.Clock
import java.util.UUID

class TransactionFactory(
    private val clock: Clock = Clock.systemUTC()
) {
    fun create(params: TransactionCreationParams): Transaction {
        // Helper to convert Params.Common to TransactionBase
        fun createBase(c: TransactionCreationParams.Common) = TransactionBase(
            id = UUID.randomUUID().toString(), // or however you make IDs
            userId = c.userId,
            accountId = c.accountId,
            amount = c.amount,
            currency = c.currency,
            date = c.date,
            categoryId = c.categoryId,
            note = c.note
        )

        return when (params) {
            is TransactionCreationParams.Expense ->
                Transaction.Expense(createBase(params.common))

            is TransactionCreationParams.Income ->
                Transaction.Income(createBase(params.common))

            is TransactionCreationParams.Transfer ->
                Transaction.Transfer(createBase(params.common), params.targetAccountId)
        }
    }
}
