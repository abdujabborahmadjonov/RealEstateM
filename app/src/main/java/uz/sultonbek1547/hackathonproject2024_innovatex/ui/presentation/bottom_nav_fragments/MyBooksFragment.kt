package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentChatsBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentMyBooksBinding


class MyBooksFragment : Fragment() {


    private val binding by lazy { FragmentMyBooksBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        return binding.root
    }

}