package com.example.recap_week5

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ChatRoomAdapter(val data: ArrayList<Chatroom>, val context: Context): BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val generatedView = inflater.inflate(R.layout.item_chatroom, null)

        val textViewName = generatedView.findViewById<TextView>(R.id.textViewRoomName)
        val textViewChat = generatedView.findViewById<TextView>(R.id.textViewRecentChat)
        val textViewGroupNumber = generatedView.findViewById<TextView>(R.id.textViewGroupSize)
        val textViewTime = generatedView.findViewById<TextView>(R.id.textViewLastTime)
        val imageViewThumbnail = generatedView.findViewById<ImageView>(R.id.imageViewProfile)
        val textViewUnread = generatedView.findViewById<TextView>(R.id.textViewUnread)

        textViewName.text = data[p0].name
        textViewChat.text = data[p0].lastChat
        textViewTime.text = data[p0].lastTime
        textViewGroupNumber.text = if(data[p0].groupNumber == 1)"" else "" + data[p0].groupNumber
        textViewUnread.text = if(data[p0].unreadmsg == 0 ) "" else "" + data[p0].unreadmsg
        textViewUnread.setTextColor(Color.RED)
        imageViewThumbnail.setImageResource(data[p0].thumbnail)

        return generatedView
    }

}