package com.example.helloworld.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.helloworld.plusApp.moreFun
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

object ImageController {
    private val db = FirebaseFirestore.getInstance()
    fun selectPhoto(activity: Activity, code:Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent,code)
    }
    fun saveImg(context: Context, email:String,uri: Uri){
        var mail_save = moreFun.splitEmail(email)
        var finalName = "perfil_image_$mail_save"
        val file = File(context.filesDir,finalName)
        //guardar nombre del archivo en DB
        //db.collection("users").document(email).set(hashMapOf("image_perfil" to finalName), SetOptions.merge())
        StorageController.saveImgStorage(email,uri)
        //gurdar archivo en almc.Local
        val perfilBytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(perfilBytes)
    }
    fun getImguri(context: Context, email:String): Uri {
        var shortmail = moreFun.splitEmail(email)
        var finalMail = "perfil_image_$shortmail"

        db.collection("users").document(email).get().addOnSuccessListener {
            var imgPerfil = it.get("image_perfil")as String?
            print("la imagen de perfil es $imgPerfil")
            finalMail = imgPerfil!!
        }

        val  file = File(context.filesDir,finalMail)
        return if (file.exists()) Uri.fromFile(file)
        else Uri.parse("android.resource://com.example.helloworld/drawable/perfildefault")
    }
}