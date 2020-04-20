package com.pap.signinwithapple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val credential = intent.getSerializableExtra("credential") as Credential
        val textView: TextView =  findViewById(R.id.textView)

        val name = credential.user?.name
        val nameText = "Name: ${name?.firstName} ${name?.lastName}"
        val email = "Email: ${credential.data.user.email}"
        val userId = "UserId: ${credential.data.user.userId}"
        val text = arrayOf(nameText, email, userId).joinToString("\n")
        textView.text = text
    }
}
