package uz.sultonbek1547.hackathonproject2024_innovatex.database

import androidx.lifecycle.LiveData
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book

class MyRemoteRepository(private val myFireBaseService: MyFirebaseService) {
    suspend fun getAllBooks(): LiveData<ArrayList<Book>> =
        myFireBaseService.getAllBooks()

    suspend fun getMyBooks(): LiveData<ArrayList<Book>> = myFireBaseService.getMyBooks()
}