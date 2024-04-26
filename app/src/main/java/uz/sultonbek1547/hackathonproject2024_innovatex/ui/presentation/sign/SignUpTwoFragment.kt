package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.sign

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentSignUpTwoBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectionDialog
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectivityManager
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.Constants
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.main.MainActivity
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference


class SignUpTwoFragment : Fragment(), ConnectionDialog.ConnectionDialogClicked {

    val binding by lazy { FragmentSignUpTwoBinding.inflate(layoutInflater) }
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private var isConnected = true

    var genIsMan = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectivityManager = ConnectivityManager(requireContext())

        val items = arrayOf(
            "10-20",
            "30-40",
            "50-60",
            "60+",

            )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etBrithDate.adapter = adapter
        binding.etBrithDate.setSelection(0)
        if (filledInformation()) {
            val name = binding.signUpFirstnameEt.text.toString()
            val surname = binding.signUpLastnameEt.text.toString()
            val number = Constants.number
            val password = Constants.password
            val email = Constants.email
            val age = binding.etBrithDate.selectedItem.toString()
            val address = binding.selectedRegionTv.text.toString()
            var gen = ""
            gen = if (genIsMan) {
                "erkak"
            } else {
                "ayol"
            }

            val user = uz.sultonbek1547.hackathonproject2024_innovatex.models.User(
                "",
                name,
                surname,
                number,
                password,
                age,
                "",
                gen,
                address,
                email
            )

            addUser(user)

        }


        binding.signUpGenManBtn.setOnClickListener { selectGen(true) }

        binding.signUpGenGirlBtn.setOnClickListener { selectGen(false) }

        binding.signUpOneNextBtn.setOnClickListener {


            findNavController().navigate(
                R.id.signInFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(findNavController().currentDestination?.id ?: 0, true)
                    .build()
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        if (refreshType == "") connectionDialog.dismissDialog()
        else if (refreshType == "notRegion") {
            connectionDialog.showDialog(
                "",
                Constants.IS_LOADING,
                "Viloyat va tumanlar yuklanmoqda..."
            )
        }
    }

    override fun connectDialogBackClicked(refreshType: String) {

    }

    private fun selectGen(isMan: Boolean) {
        if (isMan) {
            binding.signUpGenManBtn.background.setTint(requireContext().getColor(R.color.colorPrimary))
            binding.signUpGenGirlBtn.background.setTint(requireContext().getColor(R.color.not_active_gray))
            binding.signUpGenManBtn.setTextColor(Color.WHITE)
            binding.signUpGenGirlBtn.setTextColor(Color.BLACK)
            genIsMan = true
        } else {
            binding.signUpGenManBtn.background.setTint(requireContext().getColor(R.color.not_active_gray))
            binding.signUpGenGirlBtn.background.setTint(requireContext().getColor(R.color.colorPrimary))
            binding.signUpGenManBtn.setTextColor(Color.BLACK)
            binding.signUpGenGirlBtn.setTextColor(Color.WHITE)
            genIsMan = false
        }
    }

    private fun filledInformation(): Boolean {
        var filledInformation: Boolean

        when {
            binding.signUpFirstnameEt.text.toString().isNotEmpty() &&
                    binding.signUpLastnameEt.text.toString().isNotEmpty() &&
                    binding.selectedRegionTv.text.toString().isNotEmpty() &&
                    binding.etBrithDate.selectedItem.toString().isNotEmpty() -> filledInformation =
                true

            else -> {
                filledInformation = false
                if (binding.selectedCountryTv.text.toString() != "Uzbekistan") filledInformation =
                    true
                else connectionDialog.showDialog(
                    "",
                    Constants.IS_NOT_CHECKED,
                    "Viloyat va tumaningizni belgilang!"
                )
                if (binding.etBrithDate.selectedItem.toString().isEmpty())
                    connectionDialog.showDialog(
                        "",
                        Constants.IS_NOT_CHECKED,
                        "Iltimos, Tug'ilgan sanangizni belgilang!"
                    )
                if (binding.signUpLastnameEt.text.toString().isEmpty())
                    connectionDialog.showDialog(
                        "",
                        Constants.IS_NOT_CHECKED,
                        "Please write your surname"
                    )
                if (binding.signUpFirstnameEt.text.toString().isEmpty())
                    connectionDialog.showDialog(
                        "",
                        Constants.IS_NOT_CHECKED,
                        "Please, write your name"
                    )
            }
        }

        return filledInformation
    }

    private fun addUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            MyFirebaseService().postUser(user)
            withContext(Dispatchers.Main) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Toast.makeText(context, "Xush kelibsiz", Toast.LENGTH_LONG).show()
                MySharedPreference.isUserAuthenticated = true
                startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

}