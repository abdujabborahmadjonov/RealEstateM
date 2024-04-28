package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.DialogAboutProgrammerBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentUserInfoBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.ModelSimpleBook
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters.BookHorizontalAdapter


class UserInfoFragment : Fragment() {

    lateinit var books: ArrayList<ModelSimpleBook>
    lateinit var adapterbookHorizon: BookHorizontalAdapter
    private val binding by lazy { FragmentUserInfoBinding.inflate(layoutInflater) }
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        books = ArrayList()
        for (i in 0..1) {


            books.add(ModelSimpleBook("The book of names", R.drawable.book1))
            books.add(ModelSimpleBook("I got a name", R.drawable.book2))
            books.add(ModelSimpleBook("Xamsa", R.drawable.book3))
            books.add(ModelSimpleBook("Wonderful wizard", R.drawable.book4))
            books.add(ModelSimpleBook("O'zbek xalq ijodi yodgorliklari", R.drawable.book5))
        }
        adapterbookHorizon = BookHorizontalAdapter(books)
        binding.bookRv.adapter = adapterbookHorizon
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