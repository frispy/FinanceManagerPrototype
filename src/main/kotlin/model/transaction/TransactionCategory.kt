package model.transaction

import kotlinx.serialization.Serializable
import model.Identifiable

@Serializable
data class TransactionCategory(
    override val id: String,
    val name: String,
//    val iconName: String,
) : Identifiable