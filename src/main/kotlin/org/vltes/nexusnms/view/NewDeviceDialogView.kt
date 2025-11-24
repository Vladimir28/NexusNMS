package org.vltes.nexusnms.view

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.vltes.nexusnms.service.Check
import org.vltes.nexusnms.service.ConnectSSH

class NewDeviceDialogView {
    val device = SimpleStringProperty()

    //Если все ip правильные возвращаем результат объектом Result, если неверно, возвращаем ошибку
    fun deviceList(): Result<List<String>>{
        val deviceList = device.get().replace("[ \\t]+".toRegex(), "").split("\n", ",")
        for(ip in deviceList){
            if(!Check.ipAddress(ip)){
                return Result.failure(IllegalArgumentException(ip))
            }
        }
        return Result.success(deviceList)
    }

}