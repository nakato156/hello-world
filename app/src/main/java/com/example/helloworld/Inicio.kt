package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.helloworld.databinding.ActivityMainBinding
import com.example.helloworld.plusApp.moreFun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Inicio : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnLogin:Button = findViewById(R.id.btnlog)
        val btnReg:Button = findViewById(R.id.btnRegistro)
        val isInternet = moreFun.verifyInternet(this)
        btnReg.setOnClickListener(){
            //registro
            setup(isInternet)
        }
        btnLogin.setOnClickListener(){
            //login
            login(isInternet)
        }
    }
    private fun setup(isInternet:Boolean){
        title = "Autenticacion"
        val nameTxt:EditText = findViewById(R.id.txtName)
        val mailTxt:EditText = findViewById(R.id.txtMail)
        val pwdTxt:EditText = findViewById(R.id.txtPwd)
        if(mailTxt.text.isNotEmpty() && pwdTxt.text.isNotEmpty()){
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mailTxt.text.toString(), pwdTxt.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        var email:String = it.result?.user?.email ?:""
                        db.collection("users").document(email).set(
                            hashMapOf("name" to nameTxt.text.toString(),
                                      "image_perfil" to "",
                                       "telf" to "",
                                        "description" to "",
                                        "list_friend" to arrayListOf(""))
                        )
                        HomeUser(mailTxt.text.toString())
                    }else{
                        moreFun.alert(this, msg= "Por Favor Verifique su correo electronico o inicie sesion",internet=isInternet)
                    }
                }
        }
    }
    private fun login(isInternet:Boolean){
        title = "Autenticacion"
        val mailTxt:EditText = findViewById(R.id.txtMail)
        val pwdTxt:EditText = findViewById(R.id.txtPwd)
        if(mailTxt.text.isNotEmpty() && pwdTxt.text.isNotEmpty()){
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(mailTxt.text.toString(), pwdTxt.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        HomeUser(mailTxt.text.toString())
                    }else{
                        moreFun.alert(this, title="Error de Autenticacion", msg="Por Favor Verifique su correo electronico",isInternet)
                    }
                }
        }
    }

    private fun HomeUser(email:String){
        val myintent = Intent(this, UserMain::class.java).apply {
            putExtra("email",email)
        }
        startActivity(myintent)
        finish()
    }
}