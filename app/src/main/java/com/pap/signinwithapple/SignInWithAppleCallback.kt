package com.pap.signinwithapple

interface SignInWithAppleCallback {

    fun onSignInWithAppleSuccess(credential: Credential)

    fun onSignInWithAppleFailure(error: Throwable)

    fun onSignInWithAppleCancel()
}

internal fun SignInWithAppleCallback.toFunction(): (SignInWithAppleResult) -> Unit =
    { result ->
        when (result) {
            is SignInWithAppleResult.Success -> onSignInWithAppleSuccess(result.credential)
            is SignInWithAppleResult.Failure -> onSignInWithAppleFailure(result.error)
            is SignInWithAppleResult.Cancel -> onSignInWithAppleCancel()
        }
    }
