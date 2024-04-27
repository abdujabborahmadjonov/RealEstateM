package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.sultonbek1547.hackathonproject2024_innovatex.R

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
        savedInstanceState: Bundle?
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
                Snackbar.make(binding.root, "Good Job", 2000).show()
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
        var two= binding.signUpEmailEt.text.trim().substring(0,2)

        if ((binding.signUpPhoneNumberEt.text.toString().replace("-", "").length ==
                    binding.signUpPhoneNumberEt.text.trim().length) &&
            binding.signUpEmailEt.text.toString().contains("@gmail.com") &&
            (binding.signUpPasswordEt.text.length >= 8) && (two == "91") &&
            (two == "77") &&
            (two == "88") &&
            (two == "90") &&
            (two == "95") &&
            (two == "99") &&
            (two == "98") &&
            (two == "93") &&
            (two == "94") &&
            (two == "97") &&
            (two == "20") &&
            (two == "73") &&
            (binding.signUpPasswordEt.text.toString() == binding.signUpConfirmPasswordEt.toString())
        ) {
            filledInformation = true
        } else if (binding.signUpPhoneNumberEt.text.toString().replace("-", "").length ==
            binding.signUpPhoneNumberEt.text.trim().length &&
            binding.signUpEmailEt.text.toString().isEmpty() &&
            binding.signUpPasswordEt.text.length >= 8 &&
            binding.signUpPasswordEt.text.toString() == binding.signUpConfirmPasswordEt.toString()
        ) {
            filledInformation = true
        } else {
            filledInformation = true
            if (binding.signUpPhoneNumberEt.text.toString()
                    .isEmpty() || binding.signUpPhoneNumberEt.text.toString()
                    .replace("-", "").length != binding.signUpPhoneNumberEt.text.trim().length&&  two=="91"&&
                two!="77"&&
                two!="88"&&
                two!="90"&&
                two!="95"&&
                two!="99"&&
                two!="98"&&
                two!="93"&&
                two!="94"&&
                two!="97"&&
                two!="20"&&
                two!="73"
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
            } else if (binding.signUpPasswordEt.text.length < 8) {
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