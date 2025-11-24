package org.vltes.nexusnms.controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.vltes.nexusnms.service.User
import org.vltes.nexusnms.view.LoginView
import kotlin.system.exitProcess

class LoginController {
    private val viewModel = LoginView()
    @FXML
    private lateinit var passwordField: PasswordField
    @FXML
    private lateinit var loginField: TextField
    @FXML
    lateinit var closeButton: Button


    @FXML
    private fun initialize(){
        loginField.textProperty().bindBidirectional(viewModel.username)
        passwordField.textProperty().bindBidirectional(viewModel.password)
    }

    @FXML
    fun onCancel(){
        Platform.exit()
        exitProcess(0)
    }

    @FXML
    fun onAddUser(){
        viewModel.addUser()
        closeStage()
    }

    //Закрываем текущее окно
    private fun closeStage(){
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}