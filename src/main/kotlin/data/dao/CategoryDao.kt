package data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import model.transaction.TransactionCategory

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllFlow(): Flow<List<TransactionCategory>>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): TransactionCategory?

    @Query("SELECT * FROM categories WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): TransactionCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: TransactionCategory)

    @Update
    suspend fun update(category: TransactionCategory)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun delete(id: String)
}