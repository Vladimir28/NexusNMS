package org.vltes.nexusnms.controller

import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import org.vltes.nexusnms.service.Device
import org.vltes.nexusnms.service.WindowsManager
import org.vltes.nexusnms.view.MainWindowView

class MainWindowController {

    lateinit var tableListDevice: TableView<Device>
    lateinit var ip: TableColumn<Device, String>
    lateinit var name: TableColumn<Device, String>
    lateinit var model: TableColumn<Device, String>

    @FXML
    private fun initialize(){
        setupTable()
    }

    private fun setupTable(){
        ip.setCellValueFactory{it.value.ip}
        name.setCellValueFactory{it.value.name}
        model.setCellValueFactory{it.value.model}
        tableListDevice.items = MainWindowView.deviceList
    }

    @FXML
    private fun addNewCatalogClick(){

    }
    @FXML
    private fun addNewDeviceClick(){
        WindowsManager.openNewDeviceDialog()
    }
}