package org.vltes.nexusnms.service

import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.vltes.nexusnms.view.MainWindowView
import kotlin.system.exitProcess

@Suppress("JAVA_CLASS_ON_COMPANION")
class WindowsManager {

    companion object {
        //Окно процесса и результата загрузки устройств
        fun openLoadDeviceProcess(ipList: List<String>) {
            val vBox = VBox(10.0).apply {
                padding = Insets(10.0)
            }

            val hBox = HBox(10.0)
            val scroll = ScrollPane(vBox).apply {

            }

            val cancelButton = Button("Отмена")
            val confirmButton = Button("Подтвердить")
            val allDevicesAdded = SimpleBooleanProperty(false)

            confirmButton.disableProperty().bind(allDevicesAdded.not())


            val listDevice = ipList.map{ip -> Label("$ip ожидание...").also{label -> vBox.children.add(label)}}

            val deviceList = mutableListOf<Device>()

            val job = CoroutineScope(Dispatchers.Main).launch {
                ipList.forEachIndexed { i, ip ->
                    listDevice[i].text = "$ip подключение..."
                    val result = Commands.addDevice(ip)
                    listDevice[i].text = "$ip $result"
                    deviceList.add(Device(ip, "name", "model"))
                }
                allDevicesAdded.set(true)
            }

            hBox.children.addAll(cancelButton, confirmButton)
            vBox.children.add(hBox)

            val stage = Stage().apply {
                title = "Добавление устройств"
                initModality(Modality.APPLICATION_MODAL)
                scene = Scene(scroll, 300.0, 300.0)
            }

            cancelButton.setOnAction{
                job.cancel()
                stage.close()
            }
            confirmButton.setOnAction{
                MainWindowView.confirmDevice(deviceList)
            }

            stage.show()
        }

        // Окно ввода новый ip адресов
        fun openNewDeviceDialog() {
            val fxmloader = FXMLLoader(javaClass.getResource("/org/vltes/nexusnms/new-device-dialog.fxml"))
            val root: Parent = fxmloader.load()
            val stage = Stage().apply {
                isResizable = false
                scene = Scene(root, 300.0, 300.0)
            }
            stage.show()
        }

        // Окно, показывающееся при неверном вводе ip
        fun openErrorNewDevice(ipAddress: String) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Ошибка"
            alert.headerText = "Был введен неверный адрес: $ipAddress"
            alert.showAndWait()
        }

        //Окно с авторизацией администратора
        fun login(stageOwner: Stage) {
            val fxmloader = FXMLLoader(javaClass.getResource("/org/vltes/nexusnms/login.fxml"))
            val root: Parent = fxmloader.load()
            val stage = Stage().apply {
                initModality(Modality.WINDOW_MODAL)
                initOwner(stageOwner)
                isResizable = false
                scene = Scene(root, 400.0, 250.0)
            }
            stage.setOnCloseRequest {
                Platform.exit()
                exitProcess(0)
            }
            stage.showAndWait()
        }
    }
}