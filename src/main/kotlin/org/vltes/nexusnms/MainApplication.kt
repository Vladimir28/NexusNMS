package org.vltes.nexusnms

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.vltes.nexusnms.service.WindowsManager

class MainApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("main-window-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 1200.0, 700.0)
        stage.title = "NexusNMS"
        stage.scene = scene
        stage.show()
        WindowsManager.login(stage)
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}

