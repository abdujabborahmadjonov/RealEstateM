package uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.MyBookRvItemBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book

class MyBooksRecyclerviewAdapter(val function: (Book, ImageView, TextView, TextView, Int) -> Unit) :
    RecyclerView.Adapter<MyBooksRecyclerviewAdapter.Vh>() {

    var booksList: ArrayList<Book> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Vh(val itemRvBinding: MyBookRvItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(book: Book, position: Int) {
            itemRvBinding.itemImage.transitionName = book.id + "$position"
            itemRvBinding.tvBookName.transitionName = book.name + "$position"
            itemRvBinding.tvBookAuthor.transitionName = book.author + "$position"


            itemRvBinding.apply {
                tvBookName.text = book.name
                tvBookAuthor.text = book.author
                tvUserPostedDate.text = book.productPostedDataAndTime
                rate.text = "${kotlin.random.Random.nextInt(1,1000).toInt()} odam yoqtirdi"



            }
            itemRvBinding.cardView.setOnClickListener {
                itemRvBinding.itemImage.transitionName = book.id + "$position"
                itemRvBinding.tvBookName.transitionName = book.name + "$position"
                itemRvBinding.tvBookAuthor.transitionName = book.author + "$position"
                function(
                    book,
                    itemRvBinding.itemImage,
                    itemRvBinding.tvBookName,
                    itemRvBinding.tvBookAuthor,
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
        return Vh(MyBookRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(booksList[position], position)


    override fun getItemCount(): Int = booksList.size


}