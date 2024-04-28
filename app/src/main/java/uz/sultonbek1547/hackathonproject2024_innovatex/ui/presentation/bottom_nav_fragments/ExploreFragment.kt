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
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyRemoteRepository
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentExploreBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters.ExploreBooksRvAdapter
import uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels.MyBooksViewModel
import uz.sultonbek1547.hackathonproject2024_innovatex.viewmodels.MyViewModelFactory


class ExploreFragment : Fragment() {

    private val binding by lazy { FragmentExploreBinding.inflate(layoutInflater) }
    private lateinit var booksViewModel: MyBooksViewModel
    private lateinit var rvAdapter: ExploreBooksRvAdapter
    private var bookList = ArrayList<Book>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModelFactory = MyViewModelFactory(MyRemoteRepository(MyFirebaseService()))
        booksViewModel = ViewModelProvider(this, viewModelFactory).get(MyBooksViewModel::class.java)
        initRecyclerView()

        binding.swiperefresh.setOnRefreshListener {
            initRecyclerView()
            booksViewModel = ViewModelProvider(this, viewModelFactory).get(MyBooksViewModel::class.java)
            binding.swiperefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun initRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE

        rvAdapter = ExploreBooksRvAdapter() {
                selectedItem: Book, image: ImageView, tvName: TextView,
                tvAuthor: TextView,
                tvUserName: TextView, tvDescription: TextView, position: Int,
            ->
            listItemClicked(
                selectedItem, image, tvName, tvAuthor, tvUserName, tvDescription, position
            )
        }

        binding.myRv.adapter = rvAdapter

        binding.myRv.apply {
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        }


        displayBooks()
    }

    private fun displayBooks() {

        booksViewModel.productList.observe(requireActivity(), Observer {

            rvAdapter.booksList = it
            bookList = it

            if (bookList.isEmpty()) {
                binding.containerEmpty.visibility = View.VISIBLE
            } else {
                binding.containerEmpty.visibility = View.GONE
            }

            if (binding.progressBar.visibility == View.VISIBLE) {
                binding.progressBar.visibility = View.INVISIBLE
            }


        })

    }

    private fun listItemClicked(
        selectedItem: Book,
        image: ImageView,
        tvName: TextView,
        tvAuthor: TextView,
        tvUserName: TextView,
        tvDescription: TextView,
        position: Int,
    ) {

        val direction = ExploreFragmentDirections.actionExploreFragmentToBookInfoFragment(
            selectedItem,
            position.toString()
        )

        val extras = FragmentNavigatorExtras(
            image to selectedItem.imageId + "$position",
            tvName to selectedItem.name + "$position",
            tvAuthor to selectedItem.author + "$position",
            tvUserName to selectedItem.userName + "$position",
            tvDescription to selectedItem.description + "$position",

            )

        findNavController().navigate(direction, extras)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}