package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentExploreBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentMyBooksBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels.MyBooksViewModel


class ExploreFragment : Fragment() {

    private val binding by lazy { FragmentExploreBinding.inflate(layoutInflater) }
    private lateinit var booksViewModel:MyBooksViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {



        return binding.root
    }

}