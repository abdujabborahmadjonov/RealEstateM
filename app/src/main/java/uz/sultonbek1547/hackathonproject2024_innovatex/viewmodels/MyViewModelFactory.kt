package uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyRemoteRepository

class MyViewModelFactory(private val repository: MyRemoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyBooksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}