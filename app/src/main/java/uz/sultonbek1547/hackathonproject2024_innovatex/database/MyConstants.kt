package uz.sultonbek1547.hackathonproject2024_innovatex.database

import com.google.firebase.database.DatabaseReference
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User

object MyConstants {
    var deviceUser: User = User()
    lateinit var userList: ArrayList<User>
    val UZB_PHONE_NUM_PREFIX = arrayOf(
        90, 91, 93, 94, 95, 97, 88, 98, 99, 33, 73, 20
    )
    var TYPE_TEXT = "text"
    var screenLengthItem = 1
    var chatReference: DatabaseReference? = null

}