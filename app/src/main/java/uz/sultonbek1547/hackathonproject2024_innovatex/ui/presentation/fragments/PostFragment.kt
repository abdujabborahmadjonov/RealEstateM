package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.fragments

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentPostBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private val bookCategoryList = arrayListOf(
        "Badiiy adabiyotlar",
        "Psixologiya va shaxsiy rivojlanish",
        "Biznes kitoblar",
        "Bolalar adabiyoti",
        "IT sohasiga oid kitoblar",
        "Chet tilini organish",
        "Ilm-fan va darsliklar",
        "Abituriyentlar uchun kitoblar",
        "Detektiv",
        "Fantastika",
        "Siyosat",
        "Biografiya",
        "Ingliz tilidagi kitoblar",
        "Rus tilidagi kitoblar",
        "Nemis tilidagi kitoblar",
    )
    private var bookName = ""
    private var bookAuthor = ""
    private var bookCategory = ""
    private var bookDescription = ""
    private val usersCollectionRef = Firebase.firestore.collection("app_books")
    private var isImageSelected = false
    private lateinit var imageUri: Uri
    private var imageId = ""
    private var imageLink = ""
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        progressDialog = ProgressDialog(context)
        init()

        return binding.root
    }

    private fun init() {

        binding.apply {

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            edtProductCategory.setAdapter(
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    bookCategoryList
                )
            )


            btnPostProduct.setOnClickListener {
                bookName = edtProductName.text.toString().trim()
                bookAuthor = edtProductAuthor.text.toString().trim()
                bookCategory = edtProductCategory.text.toString().trim()
                bookDescription = edtDescription.text.toString().trim()

                if (isBookDataFull()) {
                    activity?.window?.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    progressDialog.setMessage("Yuklanmoqda...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    val currentDateTime =
                        SimpleDateFormat("d MMMM HH:mm", Locale("uz", "UZ")).format(
                            Date()
                        )
                    CoroutineScope(IO).launch {
                        val imageUrl = saveImagesToFirebase()
                        addBook(
                            Book(
                                "id",
                                MySharedPreference.user!!.id,
                                MySharedPreference.user!!.name,
                                imageUrl,
                                imageId,
                                bookName,
                                bookAuthor,
                                bookCategory,
                                bookDescription,
                                currentDateTime
                            )
                        )
                    }

                }
            }


            onlyViewTvSelectImageContainer.setOnClickListener {
                getMultipleImagesContent.launch("image/*")

            }

        }

    }


    private fun addBook(book: Book) = CoroutineScope(Dispatchers.IO).launch {
        try {
            MyFirebaseService().postProduct(book)

            withContext(Dispatchers.Main) {
                progressDialog.dismiss()
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Toast.makeText(context, "E'lon muvafaqqiyatli qo'shildi", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addImageViewToSelectedImagesContainer(imageUri: Uri) {
        val imageView = ImageView(context)
        imageView.setImageURI(imageUri)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        imageView.layoutParams = params
        imageView.scaleType =
            ImageView.ScaleType.CENTER_CROP // or FIT_START or FIT_END depending on your preference
        binding.selectedImagesContainer.addView(imageView)
        binding.selectedImagesContainer.visibility = View.VISIBLE

    }

    private val getMultipleImagesContent = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { images: List<Uri>? ->
        images ?: return@registerForActivityResult
        images.forEachIndexed { index, uri ->
            if (index < 1) {
                addImageViewToSelectedImagesContainer(uri)
                imageUri = uri
            }
        }
        if (images.isNotEmpty()) {
            binding.onlyViewTvSelectImageContainer.visibility = View.INVISIBLE
            isImageSelected = true
        } else {
            isImageSelected = false
        }
    }

    private fun isBookDataFull(): Boolean {
        if (!isImageSelected) {
            Toast.makeText(context, "Kitob rasmlarini yuklang", Toast.LENGTH_SHORT).show()
            return false
        }

        if (bookName.isEmpty()) {
            binding.edtProductName.error = "Kitob Nomini kiriting!"
            return false
        } else binding.edtProductName.error = null
        if (bookAuthor.isEmpty()) {
            binding.edtProductAuthor.error = "Muallif ismini kiriting!"
            return false

        } else binding.edtProductAuthor.error = null
        if (bookCategory.isEmpty()) {
            binding.edtProductCategory.error = "Tanlang!"
            return false
        } else binding.edtProductCategory.error = null
        if (bookDescription.isEmpty()) {
            binding.edtDescription.error = "Tavsif yozing!"
            return false

        } else binding.edtDescription.error = null


        return true
    }

    private suspend fun saveImagesToFirebase(): String {
        imageId = UUID.randomUUID().toString()

        imageLink = MyFirebaseService().postImageToStorage(imageId, imageUri)


        return imageLink
    }
}