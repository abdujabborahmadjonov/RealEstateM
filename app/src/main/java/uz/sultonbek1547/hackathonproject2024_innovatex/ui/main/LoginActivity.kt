package uz.sultonbek1547.hackathonproject2024_innovatex.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

    }
}