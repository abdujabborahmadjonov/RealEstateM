package uz.sultonbek1547.hackathonproject2024_innovatex.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

}