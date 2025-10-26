import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventure.Inventure
import kotlinx.coroutines.flow.Flow

@Dao
interface InventureDao {

    // Get all items, newest first
    @Query("SELECT * FROM inventures ORDER BY dateAdded DESC")
    fun getAllInventures(): Flow<List<Inventure>>

    // Get a single item by its ID
    @Query("SELECT * FROM inventures WHERE id = :id LIMIT 1")
    suspend fun getInventureById(id: Int): Inventure?

    // Add or update item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventure(inventure: Inventure)

    // Delete a specific item
    @Delete
    suspend fun deleteInventure(inventure: Inventure)

    // Delete all items
    @Query("DELETE FROM inventures")
    suspend fun deleteAllInventures()

    // Update only the quantity + refresh lastUpdated
    @Query("""
        UPDATE inventures 
        SET quantity = :newQuantity, lastUpdated = :timestamp 
        WHERE id = :id
    """)
    suspend fun updateQuantity(id: Int, newQuantity: Int, timestamp: Long = System.currentTimeMillis())

    // Search across multiple fields
    @Query("""
        SELECT * FROM inventures
        WHERE name LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
           OR supplier LIKE '%' || :query || '%'
        ORDER BY dateAdded DESC
    """)
    fun searchInventures(query: String): Flow<List<Inventure>>

    // Filter by category
    @Query("""
        SELECT * FROM inventures
        WHERE category = :category
        ORDER BY lastUpdated DESC, dateAdded DESC
    """)
    fun getInventuresByCategory(category: String): Flow<List<Inventure>>

    // Get items running low on stock
    @Query("SELECT * FROM inventures WHERE quantity <= :threshold ORDER BY quantity ASC")
    fun getLowStockItems(threshold: Int): Flow<List<Inventure>>
}
