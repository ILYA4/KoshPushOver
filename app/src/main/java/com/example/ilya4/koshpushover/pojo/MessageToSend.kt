package com.example.ilya4.koshpushover.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class MessageToSend : Serializable {

    @SerializedName("message")
    @Expose
    var message: String? = null
}
