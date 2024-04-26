package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ActivityMainBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {



    private val binding by lazy{ FragmentProfileBinding.inflate(layoutInflater)}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        binding.btnPost.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_postFragment)
        }

        return binding.root
    }

}