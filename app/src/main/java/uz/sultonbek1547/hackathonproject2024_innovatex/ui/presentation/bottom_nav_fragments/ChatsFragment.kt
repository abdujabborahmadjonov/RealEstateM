package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentChatsBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.main.ChatActivity
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters.UsersAdapter
import java.util.Locale


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var databse: FirebaseDatabase
    private lateinit var userList: ArrayList<User>
    private lateinit var usersAdapter: UsersAdapter

    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        databse = FirebaseDatabase.getInstance()
        val currentUser = MySharedPreference.user

         userList = ArrayList<User>()

        CoroutineScope(Dispatchers.IO).launch {
            // Fetch users from Firebase
            val users = MyFirebaseService().getUsersFromFirebaseAsList()

            // Filter out the current user and update UI on the main thread
            withContext(Main) {
                userList.clear()
                // Filter out the current user
                for (user in users) {
                    if (user.id != currentUser?.id && user.listOfChattedUsersId.contains(currentUser?.id)) {
                        userList.add(user)


                    }
                }

                // Update adapter and RecyclerView
                usersAdapter = UsersAdapter(userList) { user: User, position: Int ->
                    listItemClicked(user, position)
                }
                binding.myRv.adapter = usersAdapter
                binding.progressBar.visibility = View.INVISIBLE
            }
        }



//        val searchView = binding.toolbar.menu.getItem(0).actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return true
//            }
//
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                if (p0 == null || p0.trim().isEmpty()) {
//                    resetSearch()
//                    return false
//                }
//
//                val none = ArrayList(userList)
//                for (value in userList) {
//                    if (!value.name.lowercase(Locale.getDefault())
//                            .contains(p0.lowercase())
//                    ) {
//                        none.remove(value)
//                    }
//                }
//                usersAdapter = UsersAdapter(none){ user: User, position: Int ->
//                    listItemClicked(user, position)
//                }
//                binding.myRv.adapter = usersAdapter
//                return false
//            }
//        })

        return binding.root
    }

    private fun listItemClicked(user: User, position: Int) {

        val intent = Intent(context, ChatActivity::class.java).apply {
            putExtra("userId", user.id)
            putExtra("userName", user.name)
        }
        requireActivity().startActivity(intent)
    }
    fun resetSearch() {

        usersAdapter = UsersAdapter(userList){ user: User, position: Int ->
            listItemClicked(user, position)
        }
        binding.myRv.adapter = usersAdapter
    }

}