package com.example.helloworld.vo

sealed class FirebaseResult<T>{
    class Success<T>(val data:T): FirebaseResult<T>()
    class Failed<T>(val exeption: Exception): FirebaseResult<T>()
}