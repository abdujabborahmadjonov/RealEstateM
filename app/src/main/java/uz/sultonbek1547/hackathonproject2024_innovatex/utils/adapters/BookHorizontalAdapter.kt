package uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.BookGridBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.ModelSimpleBook

class BookHorizontalAdapter(var list: ArrayList<ModelSimpleBook>) :
    RecyclerView.Adapter<BookHorizontalAdapter.VH>() {
    inner class VH(var binding: BookGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(list: ModelSimpleBook, position: Int) {
           binding.tvBookName.text = list.name
           binding.image.setImageResource(list.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            BookGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        return holder.onBind(list[position], position)
    }

    interface RvTeachersClick {
        fun passNextThing(position: Int)
    }
}