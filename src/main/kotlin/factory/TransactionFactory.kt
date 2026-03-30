package factory

import model.params.TransactionCreationParams
import model.transaction.Transaction
import java.time.Clock
import java.util.UUID

class TransactionFactory(
    private val clock: Clock = Clock.systemUTC()
) : GenericFactory<Transaction, TransactionCreationParams> {

    override fun create(params: TransactionCreationParams): Transaction {
        val transactionId = UUID.randomUUID().toString()
        val currentDate = clock.instant().toString()

        return when (params) {
            is TransactionCreationParams.Transfer -> Transaction.Transfer(
                id = transactionId,
                userId = params.userId,
                accountId = params.accountId,
                targetAccountId = params.targetAccountId,
                amount = params.amount,
                currency = params.currency,
                date = currentDate,
                categoryId = params.categoryId,
                note = params.note
            )
            is TransactionCreationParams.Income -> Transaction.Income(
                id = transactionId,
                userId = params.userId,
                accountId = params.accountId,
                amount = params.amount,
                currency = params.currency,
                date = currentDate,
                categoryId = params.categoryId,
                note = params.note
            )
            is TransactionCreationParams.Expense -> Transaction.Expense(
                id = transactionId,
                userId = params.userId,
                accountId = params.accountId,
                amount = params.amount,
                currency = params.currency,
                date = currentDate,
                categoryId = params.categoryId,
                note = params.note
            )
        }
    }
}