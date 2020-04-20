package com.pap.signinwithapple

import com.google.gson.annotations.SerializedName
import java.io.Serializable;

sealed class SignInWithAppleResult {

    data class Success(val credential: Credential) : SignInWithAppleResult()

    data class Failure(val error: Throwable) : SignInWithAppleResult()

    object Cancel : SignInWithAppleResult()
}

data class Credential(val state: String, val code: String, val user: User?, val data: ResponseData): Serializable {

    data class User(val name: Name, val email: String): Serializable

    data class Name(val firstName: String, val middleName: String?, val lastName: String): Serializable

    data class ResponseData(val data: Data, val user: UserData): Serializable

    data class Data(

        @SerializedName("access_token")
        val accessToken: String,

        @SerializedName("refresh_token")
        val refreshToken: String,

        @SerializedName("id_token")
        val idToken: String

    ): Serializable

    data class UserData(
        @SerializedName("aud")
        val clientId: String,

        @SerializedName("sub")
        val userId: String,

        val email: String,

        @SerializedName("email_verified")
        val emailVerified: Boolean,

        @SerializedName("is_private_email")
        var isPrivateEmail: Boolean?

    ): Serializable
}


class SignInException(message:String): Exception(message)