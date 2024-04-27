package uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.RvItemUsersBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User

class UsersAdapter(private val list: ArrayList<User>, val function: (User, Int) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvItemUsersBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: User, position: Int) {
            itemRvBinding.itemCard.setOnClickListener {
                function(user, position)
            }

            itemRvBinding.tvName.text = user.name
//            Glide.with(itemView.context)
//                .load(user.imageLink)
//                .into(itemRvBinding.image)

//            if (user.isOnline == "true") {
//                itemRvBinding.onlineImage.visibility = View.VISIBLE
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}