package uz.sultonbek1547.hackathonproject2024_innovatex.models

import java.io.Serializable

data class Book(
    var id: String = "",
    var userId: String = "",
    var userName: String = "",
    val imageLink: String = "",
    val imageId: String = "",
    val name: String = "",
    val author: String = "",
    val category: String = "",
    val description: String = "",
    val productPostedDataAndTime: String = "",
    var userGender: String,
) : Serializable {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "", "", "", "", "","")
}
