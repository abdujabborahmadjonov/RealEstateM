package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyRemoteRepository
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentMyBooksBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters.MyBooksRecyclerviewAdapter
import uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels.MyBooksViewModel
import uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels.MyViewModelFactory


class MyBooksFragment : Fragment() {


    private lateinit var binding: FragmentMyBooksBinding
    private lateinit var booksViewModel: MyBooksViewModel
    private lateinit var rvAdapter: MyBooksRecyclerviewAdapter
    private var booksList = ArrayList<Book>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyBooksBinding.inflate(layoutInflater, container, false)
        binding.buttonBarcha.setOnClickListener{
            findNavController().navigate(R.id.analysticsFragment)
        }

        val viewModelFactory = MyViewModelFactory(MyRemoteRepository(MyFirebaseService()))
        booksViewModel = ViewModelProvider(this, viewModelFactory).get(MyBooksViewModel::class.java)
        initRecyclerView()


        return binding.root
    }

    private fun initRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        rvAdapter = MyBooksRecyclerviewAdapter {
                selectedItem: Book, image: ImageView, tvName: TextView,
                tvDescription: TextView, position: Int,
            ->
            listItemClicked(
                selectedItem, image, tvName, tvDescription, position
            )
        }
        binding.myRv.adapter = rvAdapter
        binding.myRv.apply {
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        displayProductsList()
    }

    private fun displayProductsList() {
        booksViewModel.myProductList.observe(requireActivity(), Observer {
            rvAdapter.booksList = it
            booksList = it
            if (it.isEmpty()) {
                binding.containerEmpty.visibility = View.VISIBLE
            } else {
                binding.containerEmpty.visibility = View.GONE
            }
            if (binding.progressBar.visibility == View.VISIBLE) {
                binding.progressBar.visibility = View.GONE
            }
        })

    }

    private fun listItemClicked(
        selectedItem: Book,
        imageView: ImageView,
        tvName: TextView,
        tvDescription: TextView,
        position: Int,
    ) {
        val direction =
            MyBooksFragmentDirections.actionMyBooksFragmentToBookInfoFragment(
                selectedItem, position.toString()
            )

        val extras =
            FragmentNavigatorExtras(
                imageView to selectedItem.id + "$position",
                tvName to selectedItem.name + "$position",
                tvDescription to selectedItem.description + "$position"
            )
        findNavController().navigate(direction, extras)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }


}