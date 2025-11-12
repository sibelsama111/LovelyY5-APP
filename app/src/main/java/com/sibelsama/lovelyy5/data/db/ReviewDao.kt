package com.sibelsama.lovelyy5.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY uid DESC")
    fun getReviewsForProduct(productId: Int): Flow<List<ReviewEntity>>

    @Insert
    suspend fun insert(review: ReviewEntity)
}
