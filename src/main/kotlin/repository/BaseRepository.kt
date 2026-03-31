package repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import model.Identifiable
import java.io.File

// abstract base repository for managing entities that implement Identifiable
// <T> must have an "id" property from Identifiable interface
abstract class BaseRepository<T: Identifiable> (
    private val filePath: String,
    private val serializer: KSerializer<List<T>>
)   {

    private val _itemsFlow = MutableStateFlow<List<T>>(emptyList()) // OUR STATE

    val itemsFlow: StateFlow<List<T>> = _itemsFlow.asStateFlow()

    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    // run when repository is created
    suspend fun loadData() = withContext(Dispatchers.IO) {
        val file = File(filePath)
        if (file.exists()) {
            val content = file.readText()
            val parsed = json.decodeFromString(serializer, content)
            _itemsFlow.value = parsed
        }
    }

    fun getAll(): List<T> = _itemsFlow.value

    fun getById(id: String): T? = _itemsFlow.value.find { it.id == id }

    suspend fun add(item: T) {
        val newList = _itemsFlow.value + item
        saveAndEmit(newList)
    }

    suspend fun update(item: T): Boolean {
        val newList = _itemsFlow.value.map {
            // if item with same id found
            if (it.id == item.id) {
                item // replace with updated item
            } else {
                it
            }
        }

        saveAndEmit(newList)
        return true
    }

    suspend fun delete(id: String) {
        // generate new list without value
        val newList = _itemsFlow.value.filterNot { it.id == id }
        saveAndEmit(newList)
    }

    private suspend fun saveAndEmit(newList: List<T>) {
        withContext(Dispatchers.IO) {
            val content = json.encodeToString(serializer, newList)
            File(filePath).writeText(content)
        }
        // update state (only if there was sucessful write operation)
        _itemsFlow.value = newList
    }
}
