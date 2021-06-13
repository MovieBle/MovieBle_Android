package com.example.movie.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.databinding.MovieChatListRowLayoutBinding
import com.example.movie.models.ChatModel
import com.example.movie.untils.Constants
import com.example.movie.viewmodels.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

 class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val chatList = mutableListOf<ChatModel>()
     private val db = FirebaseFirestore.getInstance()

    init {
        db.collection("chat")
            .addSnapshotListener { querySnapshot, _ ->
                // ArrayList 비워줌
                chatList.clear()

                Log.d(Constants.TAG, "데이터 들어옴: ")
                for (snapshot in querySnapshot!!.documents) {
                    val item = snapshot.toObject(ChatModel::class.java)
                    chatList.add(item!!)
                    Log.d(Constants.TAG, "데이터 들어옴 : $item ")

                    notifyDataSetChanged()
                }
                notifyDataSetChanged()
            }
    }

    inner class ChatViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding = MovieChatListRowLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_chat_list_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        with(holder) {
            binding.chatDay.text = chatList[position].chatDay
            binding.chatName.text = chatList[position].chatName
            binding.chatText.text = chatList[position].chatText
            binding.chatLikeText.text =
                chatList[position].chatLikeText.toString().toInt().toString()
        }


    }

    override fun getItemCount() = chatList.size
}