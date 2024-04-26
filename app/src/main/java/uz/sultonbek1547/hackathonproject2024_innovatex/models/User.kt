package uz.sultonbek1547.hackathonproject2024_innovatex.models

import java.io.Serializable

data class User(
    var id:String ="",
    val status: String = "",
    val classStatus: String = "",
    val name: String = "",
    val number: String = "",
    val password: String = "",
    val listOfChattedUsersId:ArrayList<String> = ArrayList()
): Serializable {
    // No-argument constructor
    constructor() : this("", "", "", "", "","",ArrayList())
}
