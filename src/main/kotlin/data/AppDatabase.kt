package data

import androidx.room.Database
import androidx.room.RoomDatabase
import data.dao.*
import data.entity.AccountEntity
import data.entity.TransactionEntity
import model.transaction.TransactionCategory
import model.user.User

@Database(
    entities = [
        User::class,
        TransactionCategory::class,
        AccountEntity::class,
        TransactionEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}