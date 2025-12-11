package org.vltes.nexusnms.controller

import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import org.vltes.nexusnms.service.Device
import org.vltes.nexusnms.service.Terminal
import org.vltes.nexusnms.service.User
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
        tableListDevice.setRowFactory{
            val row = TableRow<Device>()
            row.setOnMouseClicked{event ->
                if(!row.isEmpty && event.clickCount == 2){
                    val device = row.item
                    val ip = device.ip.value
                    val terminal = Terminal()
                    terminal.openTerminalSsh(ip, User.username, User.password)
                }
            }
            row
        }
    }

    @FXML
    private fun addNewCatalogClick(){

    }
    @FXML
    private fun addNewDeviceClick(){
        WindowsManager.openNewDeviceDialog()
    }
}