package uz.sultonbek1547.hackathonproject2024_innovatex.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference

class MyFirebaseService {
    private val usersReference = Firebase.firestore.collection("app_users")

    fun postUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val docRef = usersReference.add(user).await()
            val userId = docRef.id
            // Update the product object with the generated ID
            val updatedUser = user.copy(id = userId)
            // Update the document in Firestore with the updated product object
           // MyConstants.user = updatedUser
            MySharedPreference.user = updatedUser
            usersReference.document(userId).set(updatedUser).await()
        } catch (e: Exception) {
            Log.e("FirebaseException", "postProduct: $e")
        }

    }
}