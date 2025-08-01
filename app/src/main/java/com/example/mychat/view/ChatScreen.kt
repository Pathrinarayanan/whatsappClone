package com.example.mychat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mychat.R
import com.example.mychat.modal.BottomItem
import com.example.mychat.ui.theme.ctaBlue
import com.example.mychat.ui.theme.greyBackground
import com.example.mychat.ui.theme.lineColor
import com.example.mychat.ui.theme.textGrey


@Composable
fun BottomBar(bottomItems: List<BottomItem>, selectedIndex : Int,onClick:(Int)->Unit) {
    Row (
        Modifier.fillMaxWidth().wrapContentHeight()
            .background(greyBackground)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        bottomItems.forEachIndexed { index, item->
            BottomItemUI(item, index == selectedIndex){
                onClick(index)
            }
        }
    }
}
@Composable
fun BottomItemUI(item: BottomItem, selected: Boolean, onClick:()->Unit) {
    Column(
        Modifier.clickable{
            onClick()
        },
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(if(selected) item.selectedImg else item.img), contentDescription = null)
        Text(item.title, Modifier, color = if(selected) ctaBlue else textGrey)
    }
}




@Composable
fun ProfileItem(){
    Row(
        Modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(painter = painterResource(R.drawable.person_img),
            contentDescription = null,
            Modifier.size(52.dp).weight(0.1f))
        Column(
            Modifier.padding(start = 12.dp)
                .weight(0.8f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text("Andrew Parker", Modifier, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(1f))
                Text("28/07/2025", Modifier, color = textGrey)
            }
            Row(
                Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(R.drawable.read_icon), contentDescription = null)
                Text("what kind of strategy is better?", Modifier, color = textGrey)
            }
            Spacer(Modifier.height(1.dp).fillMaxWidth().background(lineColor))
        }
        Image(painter = painterResource(R.drawable.arrow_right),
            contentDescription = null,
            Modifier.weight(
                0.1f
            ))
    }
}

@Composable
fun ChatScreen(){
    Scaffold(
        Modifier.padding(top = 30.dp),
        topBar = {
            Row(
                Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .background(greyBackground)
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Text("Edit",Modifier, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = ctaBlue)
                Spacer(Modifier.weight(1f))
                Text("Chats", Modifier, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                Spacer(Modifier.weight(1f))
                Image(painter = painterResource(R.drawable.edit_icon), contentDescription = null)

            }
        },
        bottomBar = {
            val bottomItems = listOf<BottomItem>(
                BottomItem("Status", R.drawable.status_icon, R.drawable.status_icon_filled),
                BottomItem("Calls", R.drawable.call_icon, R.drawable.call_icon_filled),
                BottomItem("Camera", R.drawable.camera_icon, R.drawable.camera_icon),
                BottomItem("Chats", R.drawable.chat_icon, R.drawable.chat_icon_filled),
                BottomItem("Settings", R.drawable.settings_icon, R.drawable.settings_icon_filled),
            )
            var selectedIndex = remember { mutableStateOf(0) }
            BottomBar(bottomItems, selectedIndex.value){ 
                selectedIndex.value = it
            }
        }

    ) {paddingValues ->
        LazyColumn(
            Modifier.padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10){
                ProfileItem()
            }
        }
    }
}
