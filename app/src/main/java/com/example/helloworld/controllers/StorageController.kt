package com.example.helloworld.controllers

import android.net.Uri
import android.util.Log
import com.example.helloworld.plusApp.moreFun
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object StorageController {
    val folder: StorageReference = FirebaseStorage.getInstance().getReference().child("Users")

    fun saveImgStorage(user:String, image: Uri){
        val userPath = moreFun.splitEmail(user)
        val img: StorageReference =  folder.child("${userPath}/image_perfil_${userPath}")
        img.putFile(image)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("msg","yey saved sucssecfull")
            }
            .addOnFailureListener{
                Log.d("error","error bad:$it")
            }
        //folder.putStream(folder)
    }
}