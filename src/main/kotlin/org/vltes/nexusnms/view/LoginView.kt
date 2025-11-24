package org.vltes.nexusnms.view

import javafx.beans.property.SimpleStringProperty
import org.vltes.nexusnms.service.User

class LoginView {
    val username = SimpleStringProperty()
    val password = SimpleStringProperty()

    fun addUser(){
        User.username = username.value ?: "null"
        User.password = password.value ?: "null"
    }
}