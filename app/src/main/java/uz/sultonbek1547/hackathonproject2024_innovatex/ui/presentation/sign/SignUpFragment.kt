package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyConstants
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentSignUpBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectionDialog
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectivityManager
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.Constants

class SignUpFragment : Fragment(), ConnectionDialog.ConnectionDialogClicked {
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private var isConnected = true

    val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectivityManager = ConnectivityManager(requireContext())
        binding.signUpTwoBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signUpSignInBtn.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
        binding.signUpTwoNextBtn.setOnClickListener {
            if (filledInformation()) {
                Constants.number = binding.signUpPhoneNumberEt.text.toString()
                Constants.password = binding.signUpPasswordEt.text.toString()
                Constants.email = binding.signUpEmailEt.text.toString()
                findNavController().navigate(
                    R.id.action_signUpFragment_to_signUpTwoFragment
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectivityManager.observe(viewLifecycleOwner) { isConnected = it }

    }

    private fun filledInformation(): Boolean {
        var filledInformation: Boolean
        var two = 0
        if (binding.signUpPhoneNumberEt.text.length > 2){
             two = binding.signUpPhoneNumberEt.text.trim().substring(0, 2).toInt()
        }

        if (MyConstants.UZB_PHONE_NUM_PREFIX.contains(two)
        ) {
            filledInformation = true
        } else if (binding.signUpPhoneNumberEt.text.toString().replace("-", "").length ==
            binding.signUpPhoneNumberEt.text.trim().length &&
            binding.signUpEmailEt.text.toString().isEmpty() &&
            binding.signUpPasswordEt.text.isNotEmpty() &&
            binding.signUpPasswordEt.text.toString() == binding.signUpConfirmPasswordEt.toString()
        ) {
            filledInformation = true
        } else {
            filledInformation = true
            if (MyConstants.UZB_PHONE_NUM_PREFIX.contains(two)
            ) {
                filledInformation = false
                connectionDialog.showDialog(
                    "",
                    Constants.IS_NOT_CHECKED,
                    "Telefon raqamingizni to'liq\nkiriting!"
                )
            } else if (binding.signUpEmailEt.text.toString()
                    .isNotEmpty() && !binding.signUpEmailEt.text.toString().contains("@gmail.com")
            ) {
                filledInformation = false
                connectionDialog.showDialog(
                    "",
                    Constants.IS_NOT_CHECKED,
                    "Gmail pochtangizni to'g'ri\nkriting!"
                )
            } else if (binding.signUpPasswordEt.text.isEmpty()) {
                filledInformation = false
                connectionDialog.showDialog(
                    "",
                    Constants.IS_NOT_CHECKED,
                    "Parolingiz 8 xonadan kam bolmasligi kerak!"
                )
            } else if (binding.signUpPasswordEt.text.toString() != binding.signUpConfirmPasswordEt.text.toString()) {
                filledInformation = false
                connectionDialog.showDialog(
                    "",
                    Constants.IS_NOT_CHECKED,
                    "Paroli" +
                            "ngizni no'to'g'ri takrorladingiz!"
                )
            }
        }
        return filledInformation
    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        when (refreshType) {
            Constants.NO_INTERNET -> if (!isConnected) connectionDialog.showDialog(
                Constants.NO_INTERNET,
                Constants.NO_INTERNET,
                ""
            )

            else -> connectionDialog.dismissDialog()

        }
    }

    override fun connectDialogBackClicked(refreshType: String) {

    }

}