package com.example.helloworld

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toUri
import com.example.helloworld.controllers.ImageController
import com.google.firebase.firestore.FirebaseFirestore

class InfoProfile : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val coll = "users"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_profile)

        //seleccionando elementos
        val imgPerfil: ImageView = findViewById(R.id.perfilImage)
        val name:EditText = findViewById(R.id.profileName)
        val descript: EditText = findViewById(R.id.profileDescript)
        val telf: EditText = findViewById(R.id.editTextPhone)
        val btnSave: View = findViewById(R.id.saveDataP)
        val imgProfile: ImageView = findViewById(R.id.perfilImage)

        //recibiendo datos de la Activity
        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val imgUri:String? = bundle?.getString("uriImg")

        //iniciamos funciones principales
        email?.let { seterData(it,imgPerfil,imgUri!!.toUri(),name,descript,telf,imgProfile) }
        //actualizar los datos
        btnSave.setOnClickListener{
            val Username = name.text.toString()
            val UserDescript = descript.text.toString()
            val Usertelf = telf.text.toString()
            val data = hashMapOf(
                "name" to Username,
                "description" to UserDescript,
                "telf" to Usertelf
            )

            DataUser.saveDataM(email.toString(), data)
            finish()
        }
    }

    fun seterData(email:String, perfil: ImageView, uriImg: Uri, name:EditText,descript:EditText,telf:EditText,img_profile:ImageView){
        perfil.setImageURI(uriImg)
        db.collection(coll).document(email).get()
            .addOnSuccessListener { user->
                val nameUser = user.getString("name")
                val telfUser = user.getString("telf")
                val descriptUser = user.getString("description")
                val imgUri = email?.let { ImageController.getImguri(this, it) }

                img_profile.setImageURI(imgUri)
                name.setText(nameUser)
                descript.setText(descriptUser)
                telf.setText(telfUser)
            }
    }
}