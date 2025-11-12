package com.sibelsama.lovelyy5.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @param:PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val productId: Int,
    val name: String,
    val comment: String,
    val rating: Int,
    val imageUrisJson: String
)
