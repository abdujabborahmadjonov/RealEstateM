package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentProfileBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.main.LoginActivity
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference


class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        context?.let { MySharedPreference.init(it) }
        user = MySharedPreference.user!!


        init()

        binding.btnLogout.setOnClickListener {

            AlertDialog.Builder(context, R.style.RoundedCornersDialog)
                .setTitle("Diqqat")
                .setMessage("Rostdan ham tizimdan chiqmoqchimisiz?") // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(
                    "Xa"
                ) { dialog, which ->
                    // Continue with delete operation
                    MySharedPreference.isUserAuthenticated = false
                    startActivity(Intent(context, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Yo'q", null)
                .show()


        }

        return binding.root
    }

    private fun init() {
        binding.apply {
            tvUserName.text = "Salom, ${user.name}"
            tvUserPhoneNumber.text = "+998${user.number}"

        }


        binding.btnPost.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_postFragment)
        }

        binding.btnHelp.setOnClickListener {

        }

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("sultonbektolanov60@gmail.com"))
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Ilova bilan muammoga duch keldingizmi? Uni batafsil yozing.."
            )
        }
        binding.btnHelp.setOnClickListener {
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }

        binding.btnAboutApp.setOnClickListener {
//            findNavController().navigate(
//                ProfileFragmentDirections.actionProfileFragmentToAboutAppFragment()
//            )

            findNavController().navigate(R.id.action_profileFragment_to_postFragment)

        }

        binding.btnLikedBooks.setOnClickListener {
            Toast.makeText(context, "coming soon..", Toast.LENGTH_SHORT).show()
        }


    }

}