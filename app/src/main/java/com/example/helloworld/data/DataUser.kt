package com.example.helloworld

import android.content.ContentValues
import android.util.Log
import android.widget.Button
import com.example.helloworld.vo.FirebaseResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

data class User(
    var name: String? = "",
    var description: String? = null,
    var image_perfil:String? = "",
    var tokenPush: String? = null,
    var telf: String? = null,
    var list_friend: ArrayList<String>? = null
)

data class ViewFriend(
    var friends: ArrayList<String>? = null
)

object DataUser {
    val db = FirebaseFirestore.getInstance()
    val coll = "users"

    fun getNameUser(email: String, you:Button){
        db.collection(coll).document(email).get().addOnSuccessListener {
            val name = it.getString("name")
            you.setText(name)
        }
    }
    fun saveDataM(email: String, data:HashMap<String,String>){
        db.collection("users").document(email).set(data, SetOptions.merge())
    }


    suspend fun getUserData(email: String): FirebaseResult<User> = try {
        val snapshot = db.collection(coll).document(email).get().await()
        val data = snapshot.toObject(User::class.java)!!
        FirebaseResult.Success(data)
    }catch(e: Exception) {
        FirebaseResult.Failed(e)
    }

}