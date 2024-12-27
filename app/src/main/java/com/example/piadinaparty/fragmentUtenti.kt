package com.example.piadinaparty

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class fragmentUtenti : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_utenti, container, false)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        fetchUserData(view)
        return view
    }

    private fun fetchUserData(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userData = document.data
                        if (userData != null) {
                            view.findViewById<TextView>(R.id.textView7).text = "Nome: ${userData["firstName"]}"
                            view.findViewById<TextView>(R.id.textView8).text = "Cognome: ${userData["lastName"]}"
                            view.findViewById<TextView>(R.id.textView9).text = "Email: ${userData["email"]}"
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting document: ", exception)
                }
        }
    }
}