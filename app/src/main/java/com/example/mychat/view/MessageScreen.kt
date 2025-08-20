package com.example.mychat.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mychat.R
import com.example.mychat.modal.ChatMessage
import com.example.mychat.viewmodel.LoginViewModel


@Composable
fun TopBar(viewModel: LoginViewModel) {
    Row(
        Modifier.fillMaxWidth().background(Color.White)
            .padding(top = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.back),
            contentDescription = null,
            Modifier.size(12.dp)
                .weight(0.08f)
        )

        Row(Modifier.fillMaxWidth().weight(0.5f).background(Color.White)) {
            Image(
                rememberAsyncImagePainter(viewModel.base64ToBitmap(viewModel.FriendUser?.profileImage ?:"")),
                contentDescription = null,
                Modifier.size(36.dp).clip(CircleShape)
            )
            Column(
                Modifier.fillMaxWidth().padding(start = 8.dp)
            ) {
                Text(viewModel.FriendUser?.name ?:"")
                Text(viewModel.lastSeenMessage(viewModel.FriendUser?.lastSeen ?:0L))
            }
            Row(Modifier.weight(0.2f)) {
                Image(painter = painterResource(R.drawable.video_icon),
                    contentDescription = null,
                    Modifier.size(20.dp))
                Image(painter = painterResource(R.drawable.call_icon),
                    contentDescription = null,
                    Modifier
                        .padding(start =20.dp)
                        .size(20.dp))

            }
        }
    }
}

@Composable
fun BottomBarChat(viewModel: LoginViewModel){
    var text by remember { mutableStateOf("") }
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Image(painter = painterResource(R.drawable.plus_icon),
            contentDescription = null,
            Modifier.size(24.dp).padding(end = 8.dp))
        Box(Modifier.weight(1f)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))) {
            val scrollState = rememberScrollState()
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it;
                },
                Modifier.fillMaxWidth().verticalScroll(scrollState)
                    .padding(4.dp),
                maxLines = 3,
            )
            if (text.isEmpty()) {
                Text("Message", Modifier.padding(5.dp), Color.Gray)
            }

            }

            Spacer(Modifier.width(8.dp))
        this.AnimatedVisibility(visible = text.isNotBlank()) {
            Icon( Icons.Filled.Send,
                contentDescription = null,
                modifier =Modifier.size(24.dp)
                    .clickable{
                        viewModel.sendMessage(viewModel.myId?:"", viewModel.FriendUser?.uid ?:"", text)
                        text = ""
                    }
            )
        }
            this.AnimatedVisibility(visible = text.isBlank()) {
                Image(painter = painterResource(R.drawable.camera_icon),
                    contentDescription = null,
                    Modifier.size(24.dp))
            }
        this.AnimatedVisibility(visible = text.isBlank()) {
                Image(painter = painterResource(R.drawable.mic_icon),
                    contentDescription = null,
                    Modifier.padding(start =8.dp).size(24.dp))
            }
        }
    }



@Composable
fun MessageScreen(viewModel: LoginViewModel) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    LaunchedEffect(Unit) {
        viewModel.listenMessages(viewModel.myId?:"", viewModel.FriendUser?.uid ?:""){newMessages->
            messages.clear()
            messages.addAll(newMessages)
        }
    }
    Scaffold(
        Modifier,
        topBar = {
            TopBar(viewModel)
        },
        bottomBar = {
            BottomBarChat(viewModel)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .padding(it)
        ){
            Image(painter = painterResource(R.drawable.chat_background),
                contentDescription = null,
                Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            LazyColumn (
                Modifier.fillMaxSize().padding(horizontal = 8.dp)
                    .padding(vertical = 8.dp)
                , reverseLayout = true,
            ){
                itemsIndexed(messages){ index, data->
                    if(data.senderId == viewModel.myId){
                        SenderItem(data.messsage) //my message
                    }
                    else{
                        ReceiverItem(data.messsage)
                    }
                }
            }
        }
    }
}

@Composable
fun SenderItem(messsage: String) {
    Row (
        Modifier.fillMaxWidth().padding(top = 5.dp),
        horizontalArrangement = Arrangement.End
    ){
        Spacer(Modifier.weight(0.3f))
        Box(
            modifier = Modifier.weight(0.7f),
            contentAlignment = Alignment.CenterEnd
        ){
            Row {
                Text(
                    messsage,
                    Modifier.wrapContentWidth()
                        .background(
                            Color(0xffDCF7C5), RoundedCornerShape(16.dp)
                        )
                        .padding(8.dp),
                    textAlign = TextAlign.Start
                )

            }
        }
    }
}
@Composable
fun ReceiverItem(messsage: String) {
    Row (
        Modifier.fillMaxWidth().padding(top = 5.dp, end = 10.dp),
        horizontalArrangement = Arrangement.End
    ){
        Box(
            modifier = Modifier.weight(0.7f),
            contentAlignment = Alignment.CenterStart
        ){
            Text(messsage,
                Modifier.wrapContentWidth()
                    .background(Color.White, RoundedCornerShape(16.dp)
                        )
                    .padding(8.dp),
                textAlign = TextAlign.Start
                )
        }
        Spacer(Modifier.weight(0.3f))

    }
}

