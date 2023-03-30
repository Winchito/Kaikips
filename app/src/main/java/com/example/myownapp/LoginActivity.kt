package com.example.myownapp
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    companion object{
        lateinit var user: String
        lateinit var providerSession: String
    }

    private var userEmail by Delegates.notNull<String>()
    private var userPassword by Delegates.notNull<String>()
    private lateinit var textInputEmail: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    private lateinit var firebaseAuthentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textInputEmail = findViewById(R.id.textInputEmail)
        textInputPassword = findViewById(R.id.textInputPassword)
        firebaseAuthentication = FirebaseAuth.getInstance()


    }

    fun loginUser(view: View){
        getTextInputValues()
        loginMethod()
    }

    private fun getTextInputValues() {
        userEmail = textInputEmail.editText?.text.toString()
        userPassword = textInputPassword.editText?.text.toString()

    }


    private fun loginMethod(){
        firebaseAuthentication.signInWithEmailAndPassword(userEmail, userPassword).
                addOnCompleteListener(this) {
                    if(it.isSuccessful) moveToMainActivity()
                    else showErrorAlertDialog()
                }
    }


    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun changeToRegisterActivity(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
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
}