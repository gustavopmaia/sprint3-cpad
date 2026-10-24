package br.com.cultiva.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: Int,
    val highway: String,
    val status: String,
    val extension: String,
    val type: String,
    val attended: Boolean,
    val completed: Boolean,
    val hasEvidence: Boolean
)
