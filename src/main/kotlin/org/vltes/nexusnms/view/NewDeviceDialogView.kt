package org.vltes.nexusnms.view

import javafx.beans.property.SimpleStringProperty
import org.vltes.nexusnms.service.DeviceRegex

class NewDeviceDialogView {
    val device = SimpleStringProperty()

    //Если все ip правильные возвращаем результат объектом Result, если неверно, возвращаем ошибку
    fun deviceList(): Result<List<String>>{
        val deviceList = device.get().replace("[ \\t]+".toRegex(), "").split("\n", ",")
        for(ip in deviceList){
            if(!DeviceRegex.ipAddress(ip)){
                return Result.failure(IllegalArgumentException(ip))
            }
        }
        return Result.success(deviceList)
    }

}