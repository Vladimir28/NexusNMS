package org.vltes.nexusnms.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Commands {
    companion object{
        suspend fun addDevice(ipAddress: String): String = withContext(Dispatchers.IO){

            val ssh = ConnectSSH()
            val connectResult = ssh.connect(ipAddress)
            if (connectResult.isFailure) {
                println("Ошибка подключения: ${connectResult.exceptionOrNull()?.message}")
                return@withContext connectResult.exceptionOrNull()?.message ?: "Какая-то нераспознанная ошибка"
            }

            // println(ssh.send("help").getOrNull())
            /*            println(ssh.send("en").getOrNull())
                        ssh.send("conf t")
                        //println(ssh.send("terminal length 0").getOrNull())
                        println(ssh.send("sh run").getOrNull())
                        println(ssh.send("sh fut").getOrNull())*/
            ssh.disconnect()

            return@withContext "Оборудование добавлено"
        }
    }




}