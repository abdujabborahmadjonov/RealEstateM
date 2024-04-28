package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.DialogAboutProgrammerBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentAboutAppBinding


class AboutAppFragment : Fragment() {

    var dialog: AlertDialog? = null
    var dialog2: AlertDialog? = null
    lateinit var dialogBinding: DialogAboutProgrammerBinding
    private val binding by lazy { FragmentAboutAppBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.btnAbdujabbor.setOnClickListener{
            dialogabout()
        }

        binding.textabout.text = """
           1. User Profiles: Users can create profiles where they list the books they currently have and the ones they are interested in obtaining. They can also add details about their reading preferences, favorite genres, etc.
         
           2. Book Listings: Users can browse through a catalog of books available for exchange. Each book listing includes details such as title, author, genre, synopsis, condition of the book, and perhaps even user reviews or ratings.
          
           3. Search and Filter: Users can search for specific books or authors and apply filters to narrow down their search results based on criteria like genre, condition, location of the user, etc.
           
           4. Exchange Requests: Users can send exchange requests to other users for the books they are interested in. The request would include details like the book they are offering in exchange or a message to the owner of the book they want.
           
           5. Messaging System: An in-app messaging system allows users to communicate with each other to finalize exchange details such as meeting place, time, and any other arrangements.
           
           6. Rating and Reviews: After completing an exchange, users can rate and review each other based on their experience. This helps build trust within the community and ensures quality exchanges.
           
           7. Location-based Services: Integration with maps or geolocation services can help users find potential exchange partners nearby, making it easier to arrange in-person exchanges.
       """.trimIndent()

        return binding.root
    }
    fun dialogabout() {
        dialog = AlertDialog.Builder(requireContext()).create()
        dialogBinding = DialogAboutProgrammerBinding.inflate(LayoutInflater.from(requireContext()))
        dialog?.setView(dialogBinding.root)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.show()

        dialogBinding.connect.setOnClickListener {

            val url = "https://t.me/ahmadjonov_abdujabbor";
            var i = Intent(Intent.ACTION_VIEW);
            i.data = Uri.parse(url);
            startActivity(i)
            dialog?.dismiss()


        }
        dialogBinding.dismiss.setOnClickListener {
            dialog?.dismiss()

        }
    }


}