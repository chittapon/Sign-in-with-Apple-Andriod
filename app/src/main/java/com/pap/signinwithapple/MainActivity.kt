package com.pap.signinwithapple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signInWithAppleButtonBlack: SignInWithAppleButton = findViewById(R.id.sign_in_with_apple_button_black)

        val configuration = SignInWithAppleConfiguration(
            clientId = "com.pap.signinwithapple.service",
            redirectUri = "https://pap-sign-in-with-apple.herokuapp.com/callback",
            scope = "email name"
        )

        val callback: (SignInWithAppleResult) -> Unit = { result ->
            when (result) {
                is SignInWithAppleResult.Success -> {
                    showProfileActivity(result.credential)
                }
                is SignInWithAppleResult.Failure -> {
                    Log.d("APPLE_SIGN_IN", "Received error from Apple Sign In ${result.error.message}")
                }
                is SignInWithAppleResult.Cancel -> {
                    Log.d("APPLE_SIGN_IN", "User canceled Apple Sign In")
                }
            }
        }

        signInWithAppleButtonBlack.setUpSignInWithAppleOnClick(supportFragmentManager, configuration, callback)
    }

    private fun showProfileActivity(credential: Credential){
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("credential", credential)
        startActivity(intent)
    }
}
