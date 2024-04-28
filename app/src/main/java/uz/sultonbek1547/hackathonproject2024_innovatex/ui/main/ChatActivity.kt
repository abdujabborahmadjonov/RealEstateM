package uz.sultonbek1547.hackathonproject2024_innovatex.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyConstants.TYPE_TEXT
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyConstants.chatReference
import uz.sultonbek1547.hackathonproject2024_innovatex.database.MyFirebaseService
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.ActivityChatBinding
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Book
import uz.sultonbek1547.hackathonproject2024_innovatex.models.Message
import uz.sultonbek1547.hackathonproject2024_innovatex.models.MessageText
import uz.sultonbek1547.hackathonproject2024_innovatex.models.User
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.MySharedPreference
import uz.sultonbek1547.hackathonproject2024_innovatex.utils.adapters.ChatAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var imageReference: StorageReference
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var receiverId: String
    private lateinit var receiverName: String
    private var receiver: User? = User()
    private lateinit var sender: User
    private var book: Book? = Book()

    //private lateinit var chatAdapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sender = MySharedPreference.user!!
        receiverId = intent?.getStringExtra("userId").toString()
        receiverName = intent?.getStringExtra("userName").toString()
        book = intent?.getSerializableExtra("book") as Book?
        CoroutineScope(IO).launch {
            receiver = MyFirebaseService().getUserById(receiverId)
        }

        database = FirebaseDatabase.getInstance()
        reference =
            database.getReference("app_chats").child(MySharedPreference.user?.id + receiverId)



        setUpUI()

        binding.btnSend.setOnClickListener {
            val edtMessage = binding.edtMessage.text.toString().trim()
            if (edtMessage.isNotEmpty()) {
                sendMessage(edtMessage)
            }

        }

        // Set up toolbar navigation
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }



    }

    private fun setUpUI() {
        // Set up UI components
        binding.tvUserName.text = receiverName

        binding.btnSend.visibility = View.GONE
        binding.edtMessage.isActivated = true
        binding.edtMessage.isPressed = true
        binding.edtMessage.requestFocus()

        if (book != null) {
            binding.tvBookItemName.text = book!!.name
            binding.tvBookItemAuthor.text = book!!.author

            Picasso.get().load(book!!.imageLink)
                .into(binding.bookItemImage, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError(e: Exception?) {

                    }
                })
        } else {

            binding.bookItemContainer.visibility = View.GONE
        }







        binding.edtMessage.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.btnSend.visibility = View.GONE
                } else {
                    binding.btnSend.visibility = View.VISIBLE
                }
            }
        }


        chatAdapter = ChatAdapter(ArrayList(), MySharedPreference.user!!.id)
        binding.myRv.adapter = chatAdapter

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // The reference exists
                    getMessageList()
                } else {
                    // The reference does not exist
                    reference = database.getReference("app_chats")
                        .child(receiverId + MySharedPreference.user?.id)
                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(
                                    this@ChatActivity,
                                    "Xabar topilmadi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.progressBar.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                    getMessageList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })



        binding.edtMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Calculate the height based on the content of the EditText
                binding.edtMessage.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        })



    }

    private fun getMessageList() {

        // handling connection of users to show in ChatsFragment
        var receiver: User
        CoroutineScope(Dispatchers.Main).launch {
            MyFirebaseService().getUserById(receiverId)?.let {
                receiver = it
                if (!receiver.listOfChattedUsersId.contains(sender.id)) receiver.listOfChattedUsersId.add(
                    sender.id
                )
                if (!sender.listOfChattedUsersId.contains(receiver.id)) sender.listOfChattedUsersId.add(
                    receiver.id
                )
                MyFirebaseService().updateUser(receiver)
                MyFirebaseService().updateUser(sender)
                MySharedPreference.user =
                    sender // this line was missing and lead to some confusion 😅😅😅
            }

        }


        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                chatAdapter.messages.add(
                    snapshot.getValue(Message::class.java)!!
                )
                chatAdapter.notifyItemInserted(chatAdapter.itemCount - 1)
                binding.myRv.scrollToPosition(chatAdapter.itemCount - 1)
                if (binding.progressBar.visibility == View.VISIBLE) {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Message::class.java)?.let { message ->
                    chatAdapter.messages[message.positon!!.toInt()] = message
                    chatAdapter.notifyItemChanged(message.positon!!.toInt())
                    chatAdapter.messages[message.positon!!.toInt()] = message
                    chatAdapter.notifyItemChanged(message.positon!!.toInt())
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {

            }
        })

        chatReference = reference
    }

    private fun sendMessage(edtMessage: String) {

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                binding.btnSend.isEnabled = false
            }
            val messageText = MessageText()
            val key = SimpleDateFormat("dd MM yyyy  HH mm ss SSS", Locale("uz", "UZ")).format(
                Date()
            )
            messageText.id = key
            messageText.date = SimpleDateFormat("d MMMM HH:mm", Locale("uz", "UZ")).format(
                Date()
            )
            messageText.message = edtMessage
            messageText.senderId = sender.id
            messageText.receiverId = receiverId
            messageText.statusRead = if (sender.id === receiverId) "true" else "false"
            messageText.positon = chatAdapter.itemCount.toString()

            val message = Message()
            message.textOrImage = TYPE_TEXT
            message.id = messageText.id
            message.messageText = messageText
            message.senderId = sender.id
            message.receiverId = receiverId
            message.statusRead = if (sender.id === receiverId) "true" else "false"
            message.positon = chatAdapter.itemCount.toString()
            message.date = SimpleDateFormat("d MMMM HH:mm", Locale("uz", "UZ")).format(
                Date()
            )


            reference.child(key).setValue(message)

            withContext(Dispatchers.Main) {
                binding.edtMessage.text.clear()
                binding.btnSend.isEnabled = true
            }


        }


    }

}