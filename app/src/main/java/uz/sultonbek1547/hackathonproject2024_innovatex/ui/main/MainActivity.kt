package uz.sultonbek1547.hackathonproject2024_innovatex.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


    }

}