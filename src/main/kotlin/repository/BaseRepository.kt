package repository

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
    protected val items = mutableListOf<T>()
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    // runs when repository is created
    init {
        val file = File(filePath)
        // check if the file exists
        if (file.exists()) {
            val content = file.readText()
            // only try to parse if file is not empty
            if (content.isNotEmpty()) {
                try {
                    // deserialize JSON content into list of T and add to items
                    items.addAll(json.decodeFromString(serializer, content))
                } catch (e: Exception) {
                    println("Error loading data from $filePath: ${e.message}")
                }
            }
        }
    }

    fun getAll(): List<T> = items.toList()

    fun getById(id: String): T? = items.find { it.id == id }

    fun add(item: T) {
        items.add(item)
        saveToJson()
    }

    fun update(item: T): Boolean {
        val index = items.indexOfFirst { it.id == item.id }
        return if (index != -1) {
            items[index] = item
            saveToJson()
            true
        } else {
            false
        }
    }

    fun delete(id: String) {
        if (items.removeIf { it.id == id }) {
            saveToJson()
        }
    }

    // serializes the current list and writes it to the JSON file
    private fun saveToJson() {
        val content = json.encodeToString(serializer, items)
        File(filePath).writeText(content)
    }
}
