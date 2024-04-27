package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentUserInfoBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User


class UserInfoFragment : Fragment() {

    private val binding by lazy { FragmentUserInfoBinding.inflate(layoutInflater) }
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        user = arguments?.getSerializable("user") as User

        binding.apply {
            tvUserName.text = user.name
            tvUserLastSeen.text = user.lastSeen
            tvUserAddress.text = user.address
            tvUserAge.text = user.age
            tvUserPhoneNumber.text = user.number
            tvUserAddress.text = user.address

        }



        return binding.root
    }

}