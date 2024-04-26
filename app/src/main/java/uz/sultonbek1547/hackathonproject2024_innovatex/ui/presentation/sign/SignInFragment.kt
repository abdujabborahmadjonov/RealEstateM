package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.sign

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentSignInBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.ModelLoginAndPassWord
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectionDialog
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectivityManager
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.MyCustomSnackBar


class SignInFragment : Fragment(), ConnectionDialog.ConnectionDialogClicked {

    val binding by lazy { FragmentSignInBinding.inflate(layoutInflater) }
    private var isConnected = true
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var myCustomSnackBar: MyCustomSnackBar

    lateinit var logins: ArrayList<ModelLoginAndPassWord>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectionDialog = ConnectionDialog(requireContext(), this)
        connectivityManager = ConnectivityManager(requireContext())
        logins = ArrayList()

        binding.loginLogOnGuestBtn.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
        binding.loginForgotPasswordBtn.setOnClickListener {

        }
        binding.loginSignUpBtn.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
        binding.loginSignInBtn.setOnClickListener {
            myCustomSnackBar = MyCustomSnackBar(it, layoutInflater)
            getToken()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectivityManager.observe(viewLifecycleOwner) { isConnected = it }
        FirebaseApp.initializeApp(requireContext())
    }

    private fun getToken() {

        if (isConnected) {
            val username = binding.loginCountryNumTv.text.toString()
                .replace("+", "") + binding.phoneNumberEt.text.toString().replace(" ", "")
            val password = binding.passwordEt.text.toString()

            when {
                binding.phoneNumberEt.text.toString().isEmpty() -> {
                    myCustomSnackBar.showErrorSnack("Raqamingizni kiriting !")
                    binding.phoneNumberEt.requestFocus()
                }

                password.isEmpty() -> {
                    myCustomSnackBar.showErrorSnack("Parolingizni kiriting !")
                    binding.passwordEt.requestFocus()
                }

                else -> {

                    if (logins.contains(
                            ModelLoginAndPassWord(
                                binding.phoneNumberEt.text.toString(),
                                binding.passwordEt.text.toString()
                            )
                        )

                    ){
                        connectionDialog.showDialog(
                            "getToken",
                            Constants.IS_LOADING,
                            "Iltimos kuting malumotlaringiz tekshirilmoqda..."
                        )
                        Handler().postDelayed({ connectionDialog.dismissDialog() }, 2000)
                        findNavController().navigate(R.id.mainFragment)

                    }else{
                        connectionDialog.showDialog(
                            "getToken",
                            Constants.IS_NOT_CHECKED,
                            "Malumotlaringiz topilmadi."
                        )
                        Handler().postDelayed({ connectionDialog.dismissDialog() }, 2000)
                    }
                }
            }
        } else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")

    }

    override fun connectDialogRefreshClicked(refreshType: String) {
        if (refreshType == Constants.NO_INTERNET) {
            if (isConnected) connectionDialog.dismissDialog()
            else connectionDialog.showDialog(Constants.NO_INTERNET, Constants.NO_INTERNET, "")
        } else {
            getToken()
        }
    }

    override fun connectDialogBackClicked(refreshType: String) {

    }
}