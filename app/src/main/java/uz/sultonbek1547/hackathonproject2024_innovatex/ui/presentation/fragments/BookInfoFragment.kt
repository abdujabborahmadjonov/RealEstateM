package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.fragments

import android.app.AlertDialog.Builder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.BottomSheetBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentBookInfoBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.ui.main.ChatActivity
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference

class BookInfoFragment : Fragment() {

    private lateinit var binding: FragmentBookInfoBinding
    private var user = User()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBookInfoBinding.inflate(layoutInflater, container, false)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }







        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book: Book? = arguments?.getSerializable("book") as? Book
        val position: String? = arguments?.getString("position")
        binding.itemImage.transitionName = book?.id + "$position" ?: ""
        binding.tvBookName.transitionName = book?.name + "$position" ?: ""
        binding.tvBookAuthor.transitionName = book?.author + "$position" ?: ""
        binding.toolbar.transitionName = book?.userName + "$position" ?: ""
        binding.exampleContainer.transitionName = book?.description + "$position" ?: ""

        if (book?.userId == MySharedPreference.user?.id) {
            binding.btnSendMessage.visibility = View.GONE
            binding.btnUserInfo.visibility = View.GONE
            binding.btnDeleteBook.visibility = View.VISIBLE
        }

        binding.tvBookName.text = book?.name
        binding.toolbar.title = book?.userName
        binding.tvBookAuthor.text = book?.author
        binding.tvBookCategory.text = book?.category
        binding.tvBookDescription.text = book?.description

        Picasso.get()
            .load(book?.imageLink)
            .noFade()
            .into(binding.itemImage, object : Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError(e: Exception?) {
                }


            })

        binding.btnSendMessage.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra("userId", book?.userId)
                putExtra("userName", book?.userName)
            }
            requireActivity().startActivity(intent)

            binding.btnUserInfo.setOnClickListener {

                findNavController().navigate(
                    R.id.action_bookInfoFragment_to_userInfoFragment,
                    bundleOf("user" to user)
                )

                //   showCallOrSmsDialog(book!!.userName, book.userId)
            }

            binding.btnShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Kitob Nomi: ${book?.name}\nAvtor: ${book?.author}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            CoroutineScope(Dispatchers.Main).launch {
                MyFirebaseService().getUserById(book!!.userId)?.let {
                    user = it
                }

            }

            binding.btnDeleteBook.setOnClickListener {
                Builder(context, R.style.RoundedCornersDialog)
                    .setTitle("Diqqat")
                    .setMessage("Rostdan ham bu e'lonni o'chirmoqchimisiz?") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        "Xa"
                    ) { dialog, which ->
                        // Continue with delete operation
                        if (book != null) {
                            MyFirebaseService().deleteProduct(book, user)
                            findNavController().popBackStack()
                            Toast.makeText(
                                context,
                                "E'lon muvafaqqiyatli o'chirildi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Yo'q", null)
                    .show()


            }

        }
    }

    private fun showCallOrSmsDialog(userName: String, userPhoneNumber: String) {
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.apply {
            tvUserName.text = userName
            tvUserPhoneNumber.text = user.number


            btnClose.setOnClickListener {
                if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
            }

            btnCall.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${userPhoneNumber}")
                startActivity(callIntent)
            }
            btnSms.setOnClickListener {
                val smsUri = Uri.parse("smsto:${userPhoneNumber}")
                val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                smsIntent.putExtra("sms_body", "Assalomu alaykum")
                startActivity(smsIntent)
            }

        }

        bottomSheetDialog.show()

    }
}

