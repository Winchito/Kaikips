package com.example.myownapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

object Constants {
    const val DATE_FORMAT: String = "dd/MM/yyyy"
}

class RegisterActivity : AppCompatActivity() {

    private var newUserEmail by Delegates.notNull<String>()
    private var newUserPassword by Delegates.notNull<String>()
    private var newUsername by Delegates.notNull<String>()
    private lateinit var textInputEmail: TextInputLayout
    private lateinit var textInputUsername: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout
    private lateinit var firebaseAuthentication: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textInputEmail = findViewById(R.id.textInputEmail)
        textInputUsername = findViewById(R.id.textInputUsername)
        textInputPassword = findViewById(R.id.textInputPassword)
        firebaseAuthentication = FirebaseAuth.getInstance()


    }

    private fun getTextInputValues() {
        newUserEmail = textInputEmail.editText?.text.toString()
        newUserPassword = textInputPassword.editText?.text.toString()
        newUsername = textInputUsername.editText?.text.toString()
    }


    fun registerUser(view: View) {
        getTextInputValues()
        registerMethod()
    }

    private fun registerMethod() {

        println(newUserEmail)
        firebaseAuthentication.createUserWithEmailAndPassword(newUserEmail, newUserPassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    postValuesInDatabase()
                    moveToMainActivity()
                } else {
                    showErrorAlertDialog()
                }
            }
    }

    private fun postValuesInDatabase() {
        val dateRegister =
            SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(Date())
        val databaseRegister = FirebaseFirestore.getInstance()
        val id = databaseRegister.collection("users").document().id
        databaseRegister.collection("users").document(id).set(
            hashMapOf(
                "email" to newUserEmail,
                "username" to newUsername,
                "registerDate" to dateRegister
            )
        )
    }

    private fun showErrorAlertDialog() {
        val builder = AlertDialog.Builder(this)
        val layoutView = layoutInflater.inflate(R.layout.failed_layout, null)
        val buttonOk = layoutView.findViewById<Button>(R.id.btnOk)
        val textViewError = layoutView.findViewById<TextView>(R.id.textView2)
        val errorAlertString = resources.getString(R.string.error)


        builder.setView(layoutView)
        val alertDialog = builder.create()
        textViewError.text = errorAlertString
        alertDialog.show()

        buttonOk.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}