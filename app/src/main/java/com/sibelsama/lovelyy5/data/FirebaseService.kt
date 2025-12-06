package com.sibelsama.lovelyy5.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.ProductItem
import com.sibelsama.lovelyy5.model.ProductReview
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val db = FirebaseFirestore.getInstance()

    // CRUD para Productos
    suspend fun getProducts(): List<ProductItem> {
        return try {
            val snapshot = db.collection("products")
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductItem::class.java)?.copy(id = doc.id.toIntOrNull() ?: 0)
            }
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error getting products", e)
            emptyList()
        }
    }

    suspend fun getProductById(productId: Int): ProductItem? {
        return try {
            val doc = db.collection("products")
                .document(productId.toString())
                .get()
                .await()
            doc.toObject(ProductItem::class.java)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error getting product $productId", e)
            null
        }
    }

    suspend fun addProduct(product: ProductItem): Result<String> {
        return try {
            val docRef = db.collection("products").document(product.id.toString())
            docRef.set(product).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error adding product", e)
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: Int, updates: Map<String, Any>): Result<Unit> {
        return try {
            db.collection("products")
                .document(productId.toString())
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error updating product", e)
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: Int): Result<Unit> {
        return try {
            db.collection("products")
                .document(productId.toString())
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error deleting product", e)
            Result.failure(e)
        }
    }

    // CRUD para Órdenes
    suspend fun getOrders(): List<Order> {
        return try {
            val snapshot = db.collection("orders")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)
            }
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error getting orders", e)
            emptyList()
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        return try {
            val doc = db.collection("orders")
                .document(orderId)
                .get()
                .await()
            doc.toObject(Order::class.java)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error getting order $orderId", e)
            null
        }
    }

    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val docRef = if (order.id.isNotEmpty()) {
                db.collection("orders").document(order.id)
            } else {
                db.collection("orders").document()
            }

            val orderWithTimestamp = hashMapOf(
                "id" to docRef.id,
                "shippingDetails" to order.shippingDetails,
                "items" to order.items,
                "subtotal" to order.subtotal,
                "shippingCost" to order.shippingCost,
                "total" to order.total,
                "timestamp" to com.google.firebase.Timestamp.now()
            )

            docRef.set(orderWithTimestamp).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error creating order", e)
            Result.failure(e)
        }
    }

    suspend fun updateOrder(orderId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            db.collection("orders")
                .document(orderId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error updating order", e)
            Result.failure(e)
        }
    }

    suspend fun deleteOrder(orderId: String): Result<Unit> {
        return try {
            db.collection("orders")
                .document(orderId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error deleting order", e)
            Result.failure(e)
        }
    }

    // CRUD para Reviews
    suspend fun getReviewsByProductId(productId: Int): List<ProductReview> {
        return try {
            val snapshot = db.collection("reviews")
                .whereEqualTo("productId", productId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductReview::class.java)
            }
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error getting reviews", e)
            emptyList()
        }
    }

    suspend fun getAllReviews(): List<ProductReview> {
        return try {
            val snapshot = db.collection("reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductReview::class.java)
            }
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error getting all reviews", e)
            emptyList()
        }
    }

    suspend fun addReview(review: ProductReview): Result<String> {
        return try {
            val docRef = db.collection("reviews").document()
            val reviewWithTimestamp = hashMapOf(
                "productId" to review.productId,
                "userName" to review.userName,
                "rating" to review.rating,
                "comment" to review.comment,
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            docRef.set(reviewWithTimestamp).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error adding review", e)
            Result.failure(e)
        }
    }

    suspend fun updateReview(reviewId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            db.collection("reviews")
                .document(reviewId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error updating review", e)
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            db.collection("reviews")
                .document(reviewId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseService", "Error deleting review", e)
            Result.failure(e)
        }
    }
}

