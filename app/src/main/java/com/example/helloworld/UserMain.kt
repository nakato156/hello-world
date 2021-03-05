package com.example.helloworld

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.core.os.bundleOf
import com.example.helloworld.controllers.ImageController
import com.example.helloworld.controllers.StorageController
import com.example.helloworld.data.FragmentList
import com.example.helloworld.plusApp.moreFun
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class UserMain : AppCompatActivity(){
    private val PHOTO_CODE = 100
    private var perfilUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        //recibiendo datos de la Activity
        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val fragment = FragmentList().apply {
            arguments = bundleOf("email" to email)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.compose_view, fragment)
            .commit()

        val btnYou:Button = findViewById(R.id.btnYou)
        val img_perfil:ImageView = findViewById(R.id.imgPerfil)

        //cargar datos principales al iniciar
        email?.let { init(it.toString(), img_perfil,btnYou) }

        //modificar foto de perfil
        img_perfil.setOnClickListener(){
            ImageController.selectPhoto(this,PHOTO_CODE)
        }
    }

//activity para cambiar foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val img_perfil:ImageView = findViewById(R.id.imgPerfil)
        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        when {
            requestCode == PHOTO_CODE && resultCode == Activity.RESULT_OK ->{
                perfilUri = data!!.data
                img_perfil.setImageURI(perfilUri)
                perfilUri?.let {
                    StorageController.saveImgStorage(email!!, it)
                }
            }
        }
        val imgUri = email?.let { ImageController.getImguri(this, it) }
        img_perfil.setImageURI(imgUri)
    }

    private fun init(email:String, img_perfil:ImageView,you:Button){
        //mostrar foto de perfil
        val imgUri = email?.let { ImageController.getImguri(this, it) }

        img_perfil.setImageURI(imgUri)

        DataUser.getNameUser(email,you)
        //evento del boton
        you.setOnClickListener{
            //view_perfil(email,imgTest)
        }
        //creando y obtenindo el Token para las notificacione
        tokenNotify(email)
    }

    private fun tokenNotify(email: String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                moreFun.alert(this, title="Error al generar token" ,msg ="Fetching FCM registration token failed ${task.exception}")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            val tokenUser = token.toString()

            // save token in database
            val tokenMap = hashMapOf("tokenPush" to tokenUser)
            DataUser.saveDataM(email,tokenMap)
        })
    }
    private fun view_perfil(email:String, uriImg: Uri){
        val myintent = Intent(this, InfoProfile::class.java).apply {
            putExtra("email",email)
            putExtra("uriImg", uriImg.toString())
        }
        startActivity(myintent)
    }
}