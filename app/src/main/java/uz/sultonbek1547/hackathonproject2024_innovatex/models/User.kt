package uz.sultonbek1547.hackathonproject2024_innovatex.models

import java.io.Serializable

data class User(
    var id:String ="",
    val name: String = "",
    val surname: String = "",
    val number: String = "",
    val password: String = "",
    val age: String = "",
    val status: String = "",
    val gender: String = "",
    val address: String = "",
    val email: String = "",
    var lastSeen:String = "",
    val listOfChattedUsersId:ArrayList<String> = ArrayList()
): Serializable {
    constructor(): this(
        id = "",
        name = "",
        surname = "",
        number = "",
        password = "",
        age = "",
        status = "",
        gender = "",
        address = "",
        email = "",
        lastSeen = "",
        listOfChattedUsersId = ArrayList()
    )
}