package uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyConstants.chatReference
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ItemReceivedMessageBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ItemSentMessageBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Message

class ChatAdapter(val messages: ArrayList<Message>, private val senderId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SENT_MESSAGE = 1
    private val RECEIVED_MESSAGE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SENT_MESSAGE -> {
                val binding = ItemSentMessageBinding.inflate(layoutInflater, parent, false)
                SentMessageViewHolder(binding)
            }

            RECEIVED_MESSAGE -> {
                val binding = ItemReceivedMessageBinding.inflate(layoutInflater, parent, false)
                ReceivedMessageViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            SENT_MESSAGE -> {
                val sentHolder = holder as SentMessageViewHolder
                sentHolder.bind(message)
            }

            RECEIVED_MESSAGE -> {
                val receivedHolder = holder as ReceivedMessageViewHolder
                receivedHolder.bind(message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == senderId) {
            SENT_MESSAGE
        } else {
            RECEIVED_MESSAGE
        }
    }

    class SentMessageViewHolder(private val binding: ItemSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // Bind the sent message data to the views in the sent message layout using view binding
            binding.tvMessage.text = message.messageText!!.message
            binding.tvMessageContent.text = message.date
            Log.i("Library_chat", "bind: ${message.messageText!!.message}")

            if (message.statusRead == "true") {
                binding.imgMessageStatus.setImageResource(R.drawable.single_checkmark)
            } else {
                binding.imgMessageStatus.setImageResource(R.drawable.single_checkmark)
            }
            //  binding.constraintLayout.visibility = View.GONE
            //   binding.imageView.visibility = View.VISIBLE
//                Picasso.get().load(message.messageImage!!.imageLink).into(binding.imageView)

        }
    }

    class ReceivedMessageViewHolder(private val binding: ItemReceivedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // Bind the received message data to the views in the received message layout using view binding
            binding.tvMessage.text = message.messageText!!.message
            Log.i("Library_chat", "bind: ${message.messageText!!.message}")
            binding.tvMessageContent.text = message.date
            if (message.statusRead != "true") {
                message.statusRead = "true"
                chatReference!!.child(message.id!!).setValue(message)
            }
            //   binding.imageView.visibility = View.VISIBLE
            //    Picasso.get().load(message.messageImage!!.imageLink).into(binding.imageView)


        }
    }

}
