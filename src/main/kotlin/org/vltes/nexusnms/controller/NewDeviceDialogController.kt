package org.vltes.nexusnms.controller

import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.scene.control.Label
import kotlinx.coroutines.CoroutineScope
import org.vltes.nexusnms.service.ConnectSSH
import org.vltes.nexusnms.service.WindowsManager
import org.vltes.nexusnms.view.NewDeviceDialogView

class NewDeviceDialogController {
    private val viewModel = NewDeviceDialogView()
    lateinit var mainBorder: BorderPane
    lateinit var mainBox: VBox

    @FXML
    lateinit var textAreaDevise: TextArea
    @FXML
    lateinit var closeButton: Button


    @FXML
    private fun initialize(){
        textAreaDevise.textProperty().bindBidirectional(viewModel.device)
    }
    @FXML
    fun onCancel(){
        closeStage()
    }
    fun onAddDevice(){
        val resultIpList = viewModel.deviceList()
        if(resultIpList.isFailure){
            val ip = resultIpList.exceptionOrNull()?.message ?: "Ошибка"
            textAreaDevise.clear()
            WindowsManager.openErrorNewDevice(ip)
        }
        else{
            WindowsManager.openLoadDeviceProcess(resultIpList.getOrThrow())
            //viewModel.addDevice(resultIpList.getOrThrow().first())
            //println(resultIpList.getOrThrow().first())
            closeStage()
        }
    }
    //Закрываем текущее окно
    private fun closeStage(){
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}