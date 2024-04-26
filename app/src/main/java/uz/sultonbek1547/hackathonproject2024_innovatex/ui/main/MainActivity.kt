package uz.sultonbek1547.hackathonproject2024_innovatex.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ActivityMainBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // false means user have not logged in yet
        if (MySharedPreference.isUserAuthenticated != true) {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        } else {
            setContentView(binding.root)
            setupBottomNavigationBar()
        }



    }

    private fun setupBottomNavigationBar() {
        val navController = findNavController(R.id.main_navigation)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_explore -> {
                    navController.navigate(R.id.exploreFragment)
                    true
                }
                R.id.navigation_chats -> {
                    navController.navigate(R.id.chatsFragment)
                    true
                }
                R.id.navigation_my_books -> {
                    navController.navigate(R.id.myBooksFragment)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }

    }

}