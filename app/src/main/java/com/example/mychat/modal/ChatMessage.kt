package com.example.mychat.modal

import com.example.mychat.view.MessageScreen

data class ChatMessage(
    val senderId : String = "",
    val receiverId : String  = "",
    val messsage : String =  "",
    val timestamp : Long = System.currentTimeMillis(),
    val status : MessageStatus = MessageStatus.SENT
)
