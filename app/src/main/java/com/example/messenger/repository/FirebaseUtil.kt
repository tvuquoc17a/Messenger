package com.example.messenger.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtil {
    val auth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val database : FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    val storage : FirebaseStorage by lazy { FirebaseStorage.getInstance() }
}