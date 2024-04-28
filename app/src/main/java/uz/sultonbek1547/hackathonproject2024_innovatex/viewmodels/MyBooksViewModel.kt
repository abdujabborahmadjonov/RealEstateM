package uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyRemoteRepository
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book

class MyBooksViewModel(private val myRemoteRepository: MyRemoteRepository) : ViewModel() {
    val productList = MutableLiveData<ArrayList<Book>>()
    val myProductList = MutableLiveData<ArrayList<Book>>()

    init {
        loadProducts()
    }

     fun loadProducts() {
        viewModelScope.launch {
            productList.value = myRemoteRepository.getAllBooks().value
            myProductList.value = myRemoteRepository.getMyBooks().value
        }
    }
}