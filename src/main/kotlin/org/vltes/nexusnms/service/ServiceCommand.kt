package org.vltes.nexusnms.service

import net.schmizz.sshj.transport.TransportException
import net.schmizz.sshj.userauth.UserAuthException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ServiceCommand {
    companion object{
        fun errorHandler(result: Result<String>) = when(result.exceptionOrNull()){
            is SocketTimeoutException -> "Превышено время ожидания"
            is ConnectException -> "Соединение не установлено, устройство не в сети"
            is UserAuthException -> "Ошибка аутентификации. Неверный логин или пароль"
            is TransportException -> "Разрыв соединения"
            else -> "Неизвестная ошибка"
        }
    }
}