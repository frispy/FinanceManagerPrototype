package model.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import model.Identifiable

@Entity(tableName = "categories") // mixed entity and table
@Serializable
data class TransactionCategory(
    @PrimaryKey override val id: String,
    val name: String,
) : Identifiable