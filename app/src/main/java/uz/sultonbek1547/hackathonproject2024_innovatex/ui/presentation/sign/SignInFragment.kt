package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.sign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentSignInBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectionDialog
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.ConnectivityManager
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.Constants
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.constants.MyCustomSnackBar
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.main.MainActivity


class SignInFragment : Fragment(), ConnectionDialog.ConnectionDialogClicked {

    private val binding by lazy { FragmentSignInBinding.inflate(layoutInflater) }
    private var isConnected = true
    private lateinit var connectionDialog: ConnectionDialog
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var myCustomSnackBar: MyCustomSnackBar
    private var listOfUsers = ArrayList<User>()
    lateinit var usersCollectionRef:CollectionReference


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FirebaseApp.initializeApp(requireContext())
         usersCollectionRef = Firebase.firestore.collection("app_users")

        connectionDialog = ConnectionDialog(requireContext(), this)
        connectivityManager = ConnectivityManager(requireContext())
        getUsers()

        MyFirebaseService().postUser(User())

        binding.loginLogOnGuestBtn.setOnClickListener {
            //
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

    private fun getUsers() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val list = ArrayList<User>()
            val querySnapshot = usersCollectionRef.get().await()
            for (document in querySnapshot.documents) {
                document.toObject(User::class.java)?.let { user ->
                    list.add(user)
                }
            }
            listOfUsers = list
            Log.i("Library12", "getUsers: ${list.toString()}")

        } catch (e: java.lang.Exception) {
            withContext(Dispatchers.Main) {
                Log.i("Library12", "getUsers: ${e.message}")
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectivityManager.observe(viewLifecycleOwner) { isConnected = it }
        FirebaseApp.initializeApp(requireContext())
    }

    private fun getToken() {

        if (isConnected) {
            val password = binding.passwordEt.text.toString()
            val phoneNumber = binding.phoneNumberEt.text.toString()

            when {
                phoneNumber.isEmpty() -> {
                    myCustomSnackBar.showErrorSnack("Raqamingizni kiriting !")
                    binding.phoneNumberEt.requestFocus()
                }

                password.isEmpty() -> {
                    myCustomSnackBar.showErrorSnack("Parolingizni kiriting !")
                    binding.passwordEt.requestFocus()
                }

                else -> {

                    var status = true
                    listOfUsers.forEach {
                        if (it.number == phoneNumber && it.password == password) {
                            // correct move to MainActivity
                            startActivity(Intent(context, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })

                            status = false
                        }
                    }
                    if (status){
                        Toast.makeText(context, "Parol yoki nomer xato", Toast.LENGTH_SHORT).show()
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