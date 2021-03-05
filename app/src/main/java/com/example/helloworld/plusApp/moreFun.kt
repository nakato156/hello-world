package com.example.helloworld.plusApp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog

object moreFun {
    fun alert(context: Context, title:String =  "Error de registro", msg:String,internet:Boolean=true){
        var newtitle=title; var newmsg=msg
        if (!internet){
            newtitle = "Error de Conexión"
            newmsg = "Verifica tu conexión a Internet"
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(newtitle)
        builder.setMessage(newmsg)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun splitEmail(email:String):String{
        var splited= Regex("@").split(email)
        var first = splited[0]
        return first
    }

    fun verifyInternet(context: Context): Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

}