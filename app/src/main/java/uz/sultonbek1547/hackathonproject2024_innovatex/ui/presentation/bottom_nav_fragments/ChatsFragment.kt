package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {

    private val binding by lazy{ FragmentChatsBinding.inflate(layoutInflater)}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        return binding.root
    }

}