package uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.BookRvItemBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book

class ExploreBooksRvAdapter(val function: (Book, ImageView, TextView, TextView, TextView, TextView, Int) -> Unit) :
    RecyclerView.Adapter<ExploreBooksRvAdapter.Vh>() {

    var booksList: ArrayList<Book> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Vh(val itemRvBinding: BookRvItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(book: Book, position: Int) {
            itemRvBinding.itemImage.transitionName = book.id+"$position"
            itemRvBinding.tvBookName.transitionName = book.name+"$position"
            itemRvBinding.tvBookAuthor.transitionName = book.author+"$position"
            itemRvBinding.tvUserName.transitionName = book.userName+"$position"
            itemRvBinding.tvUserPostedDate.transitionName = book.description+"$position"


            itemRvBinding.apply {
                tvUserName.text = book.userName
                tvUserPostedDate.text = book.productPostedDataAndTime
                tvBookName.text = book.name
                tvBookAuthor.text = book.author
            }
            itemRvBinding.cardView.setOnClickListener {
                itemRvBinding.itemImage.transitionName = book.id+"$position"
                itemRvBinding.tvBookName.transitionName = book.name+"$position"
                itemRvBinding.tvBookAuthor.transitionName = book.author+"$position"
                itemRvBinding.tvUserName.transitionName = book.userName+"$position"
                itemRvBinding.tvUserPostedDate.transitionName = book.description+"$position"
                function(
                    book,
                    itemRvBinding.itemImage,
                    itemRvBinding.tvBookName,
                    itemRvBinding.tvBookAuthor,
                    itemRvBinding.tvUserName,
                    itemRvBinding.tvUserPostedDate,
                    position
                )
            }
            Picasso.get().load(book.imageLink)
                .into(itemRvBinding.itemImage, object : Callback {
                    override fun onSuccess() {
                        itemRvBinding.progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {

                    }
                })


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(BookRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(booksList[position], position)


    override fun getItemCount(): Int = booksList.size


}