package org.vltes.nexusnms.service

import javafx.beans.property.SimpleStringProperty

class Device(ip: String, name: String, model: String) {
    val ip = SimpleStringProperty(ip)
    val name = SimpleStringProperty(name)
    val model = SimpleStringProperty(model)
}