package org.vltes.nexusnms.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.vltes.nexusnms.service.Device

object MainWindowView {

    val deviceList: ObservableList<Device> = FXCollections.observableArrayList()

    fun confirmDevice(deviceList: List<Device>) {
        this.deviceList.setAll(deviceList)
    }
}