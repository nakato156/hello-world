package com.example.helloworld.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.helloworld.DataUser.getUserData
import com.example.helloworld.User
import com.example.helloworld.plusApp.moreFun
import com.example.helloworld.vo.FirebaseResult
import kotlinx.coroutines.*

class FragmentList : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ComposeView(requireContext())

        view.apply {
            val user = arguments?.getString("email")
            setContent {
                val (userState, updateState) = remember {
                    mutableStateOf<FirebaseResult<User>?>(null)
                }
                when(userState) {
                    null -> {
                        CircularProgressIndicator()
                        rememberCoroutineScope().launch {
                            updateState(getUserData(user!!))
                        }
                    }
                    is FirebaseResult.Failed -> {
                        val e = userState.exeption
                        Text(text = "error ${e.message}")
                    }
                    is FirebaseResult.Success -> {
                        val user = userState.data
                        //val UserPhoto = user.image_perfil
                        viewfriends(user = user)
                    }
                }

            }
            //val imgUser: ImageView = findViewById(R.id.imgPerfil)
            //if(UserPhoto!="" || UserPhoto!=null) imgUser.setImageURI(UserPhoto?.toUri())
        }
        return view
    }
}

 @Composable
fun viewfriends(user:User){
    var btnx = 10.dp
    val friends = user.list_friend
    Row(modifier = Modifier
        .padding(16.dp)
        .offset(y = 250.dp), horizontalArrangement = Arrangement.Center
    ) {
        for (friend in friends!!) {
            var friend = moreFun.splitEmail(friend)
            val btnMod = Modifier
                .size(130.dp)
                .clip(CircleShape)
                //.offset(x = btnx, y = 100.dp)
            Button(onClick = {  }, modifier = btnMod) {
                Text(friend)
                Spacer(modifier = Modifier.height(30.dp).padding(10.dp))
            }
            btnx = btnx +16.dp
        }
    }
}
