package com.pap.signinwithapple

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import com.pap.signinwithapple.SignInWithAppleButton.Companion.SIGN_IN_WITH_APPLE_LOG_TAG
import com.google.gson.Gson

@SuppressLint("SetJavaScriptEnabled")
internal class SignInWebViewDialogFragment : DialogFragment() {

    companion object {
        private const val AUTHENTICATION_ATTEMPT_KEY = "authenticationAttempt"
        private const val WEB_VIEW_KEY = "webView"

        fun newInstance(authenticationAttempt: SignInWithAppleService.AuthenticationAttempt): SignInWebViewDialogFragment {
            val fragment = SignInWebViewDialogFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(AUTHENTICATION_ATTEMPT_KEY, authenticationAttempt)
            }
            return fragment
        }
    }

    private lateinit var authenticationAttempt: SignInWithAppleService.AuthenticationAttempt
    private var callback: ((SignInWithAppleResult) -> Unit)? = null

    fun configure(callback: (SignInWithAppleResult) -> Unit) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationAttempt = arguments?.getParcelable(AUTHENTICATION_ATTEMPT_KEY)!!
        setStyle(STYLE_NORMAL, R.style.sign_in_with_apple_button_DialogTheme)
    }

    @SuppressLint("JavascriptInterface")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val webView = WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                addJavascriptInterface(JavascriptInterface(), "WebViewJS")
            }
        }

        if (savedInstanceState != null) {
            savedInstanceState.getBundle(WEB_VIEW_KEY)?.run {
                webView.restoreState(this)
            }
        } else {
            webView.loadUrl(authenticationAttempt.authenticationUri)
        }

        return webView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        onCallback(SignInWithAppleResult.Cancel)
    }

    // SignInWithAppleCallback

    private fun onCallback(result: SignInWithAppleResult) {
        dialog?.dismiss()
        val callback = callback
        if (callback == null) {
            Log.e(SIGN_IN_WITH_APPLE_LOG_TAG, "Callback is not configured")
            return
        }
        callback(result)
    }

    private inner class JavascriptInterface
    {
        @android.webkit.JavascriptInterface
        fun webResponse(body: String) {
            Log.d(WEB_VIEW_KEY, body)
            val credential = Gson().fromJson(body, Credential::class.java)
            onCallback(SignInWithAppleResult.Success(credential))
        }

        @android.webkit.JavascriptInterface
        fun webError(body: String) {
            Log.d(WEB_VIEW_KEY, body)
            onCallback(SignInWithAppleResult.Failure(SignInException(body)))
        }
    }
}