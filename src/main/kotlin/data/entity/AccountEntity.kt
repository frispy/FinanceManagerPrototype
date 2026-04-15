package data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import model.account.Account
import model.account.BankAccount
import model.account.CashAccount
import model.account.DepositAccount
import model.enum.AccountType
import model.enum.CurrencyType

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val balance: Long,
    val currency: CurrencyType,
    val accountType: AccountType,
    // specific fields
    val bankName: String? = null,
    val cashLocation: String? = null,
    val dailyLimit: Long? = null,
    val interestRate: Double? = null
)

// map to model
fun AccountEntity.toDomain(): Account = when (accountType) {
    AccountType.BANK -> BankAccount(userId, balance, currency, bankName ?: "", id)
    AccountType.CASH -> CashAccount(userId, balance, currency, cashLocation ?: "", dailyLimit ?: 0L, id)
    AccountType.DEPOSIT -> DepositAccount(userId, balance, currency, interestRate ?: 0.0, id)
}

// map to entity
fun Account.toEntity(): AccountEntity = when (this) {
    is BankAccount -> AccountEntity(id, userId, balance, currency, accountType, bankName = bankName)
    is CashAccount -> AccountEntity(id, userId, balance, currency, accountType, cashLocation = cashLocation, dailyLimit = dailyLimit)
    is DepositAccount -> AccountEntity(id, userId, balance, currency, accountType, interestRate = interestRate)
}