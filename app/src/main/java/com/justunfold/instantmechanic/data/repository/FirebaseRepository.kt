package com.justunfold.instantmechanic.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.justunfold.instantmechanic.domain.model.ServiceRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun getOrCreateUserId(): String {
        return auth.currentUser?.uid ?: run {
            val result = auth.signInAnonymously().await()
            result.user?.uid ?: throw IllegalStateException("Firebase Auth failed")
        }
    }

    suspend fun submitServiceRequest(request: ServiceRequest): Result<Unit> {
        return try {
            val uid = getOrCreateUserId()
            val finalRequest = request.copy(userId = uid)
            firestore.collection("service_requests")
                .add(finalRequest)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserBookings(): Flow<List<ServiceRequest>> = callbackFlow {
        val uid = auth.currentUser?.uid
        var listenerRegistration: ListenerRegistration? = null

        if (uid != null) {
            listenerRegistration = firestore.collection("service_requests")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val requests = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(ServiceRequest::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    trySend(requests)
                }
        } else {
            trySend(emptyList())
        }

        awaitClose { listenerRegistration?.remove() }
    }
}
