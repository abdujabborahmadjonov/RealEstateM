package uz.sultonbek1547.hackathonproject2024_innovatex.database

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference

class MyFirebaseService {
    private val productsReference = Firebase.firestore.collection("app_books")
    private val usersReference = Firebase.firestore.collection("app_users")
    private val imagesReference = FirebaseStorage.getInstance().getReference("productImages")

    fun removeDuplicateBooks() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = productsReference.get().await()
            val booksMap =
                HashMap<String, Book>() // HashMap to store unique books based on their IDs
            for (document in querySnapshot.documents) {
                val book = document.toObject(Book::class.java)
                if (book != null) {
                    // Check if the book ID already exists in the map
                    if (!booksMap.containsKey(book.id)) {
                        // If not, add it to the map
                        booksMap[book.id] = book
                    } else {
                        // If a duplicate is found, delete it from Firestore
                        productsReference.document(book.id).delete().await()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseException", "removeDuplicateBooks: $e")
        }
    }


    fun postProduct(book: Book) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val docRef = productsReference.add(book).await()
            val productId = docRef.id
            // Update the product object with the generated ID
            val updatedProduct = book.copy(id = productId)
            // Update the document in Firestore with the updated product object
            productsReference.document(productId).set(updatedProduct).await()
        } catch (e: Exception) {
            Log.e("FirebaseException", "postProduct: $e")
        }

    }

    fun postUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val docRef = usersReference.add(user).await()
            val userId = docRef.id
            // Update the product object with the generated ID
            val updatedUser = user.copy(id = userId)
            // Update the document in Firestore with the updated product object
            updatedUser.also { MyConstants.user = it }
            MySharedPreference.user = updatedUser
            usersReference.document(userId).set(updatedUser).await()
        } catch (e: Exception) {
            Log.e("FirebaseException", "postProduct: $e")
        }

    }


    suspend fun postImageToStorage(id: String, uri: Uri): String =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val imageUrlDeferred = CompletableDeferred<String>()
            try {
                val task = imagesReference.child(id).putFile(uri)
                task.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        // link of image is ready
                        val imageUrl = it.toString()
                        imageUrlDeferred.complete(imageUrl)
                    }
                }
                task.await()
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
            }
            imageUrlDeferred.await()
        }

    suspend fun getAllBooks(): LiveData<ArrayList<Book>> =
        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = productsReference.get().await()
                val listOfProducts = MutableLiveData<ArrayList<Book>>(ArrayList())
                for (document in querySnapshot.documents) {
                    document.toObject(Book::class.java)?.let {
                        if (it.userId != MySharedPreference.user?.id) {
                            listOfProducts.value!!.add(it)
                        }
                    }
                }
                return@withContext listOfProducts
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
                return@withContext MutableLiveData<ArrayList<Book>>()
            }
        }

    suspend fun getMyBooks(): LiveData<ArrayList<Book>> =
        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = productsReference.get().await()
                val listOfProducts = MutableLiveData<ArrayList<Book>>(ArrayList())
                for (document in querySnapshot.documents) {
                    document.toObject(Book::class.java)?.let {
                        if (it.userId == MySharedPreference.user?.id) {
                            listOfProducts.value!!.add(it)
                        }
                    }
                }
                return@withContext listOfProducts
            } catch (e: Exception) {
                Log.e("FirebaseException", "getProductsAsync: $e")
                return@withContext MutableLiveData<ArrayList<Book>>()
            }
        }

    fun updateUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        user.id.let { usersReference.document(it).set(user) }
    }

    suspend fun getUserById(userId: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = usersReference.document(userId).get().await()
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.id = documentSnapshot.id
                    user
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("FirebaseException", "getUserById: $e")
                null
            }
        }
    }


    suspend fun getUsersFromFirebaseAsList(): List<User> {
        return try {
            val querySnapshot = usersReference.get().await()
            val userList = mutableListOf<User>()
            for (document in querySnapshot.documents) {
                document.toObject(User::class.java)?.let {
                    userList.add(it)
                }
            }
            userList
        } catch (e: Exception) {
            Log.e("FirebaseException", "getProductsAsync: $e")
            emptyList() // Return an empty list in case of failure
        }
    }


    fun deleteProduct(book: Book, user: User) = CoroutineScope(Dispatchers.IO).launch {
//        if (MyConstants.likedProductsList!!.contains(product)) {
//            MyConstants.likedProductsList!!.remove(
//                product
//            )
//        }
        //  user.likedProducts = Gson().toJson(MyConstants.likedProductsList)
        //updateUser(user)
        // MySharedPreference.user = user
        productsReference.document(book.id).delete().await()
        Log.i("LibraryImage", "deleteProduct: ${book.imageId}")
        imagesReference.child(book.imageId).delete().await()

    }

}