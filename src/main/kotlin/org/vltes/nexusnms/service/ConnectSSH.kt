package org.vltes.nexusnms.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import java.io.InputStream
import java.io.OutputStream


class ConnectSSH {

    private lateinit var ssh: SSHClient
    private lateinit var session: Session
    private lateinit var shell: Session.Shell
    private lateinit var input: InputStream   // читаем вывод
    private lateinit var output: OutputStream// пишем команды

    suspend fun connect(ip: String): Result<String> = withContext(Dispatchers.IO){
        try {
            val client = SSHClient().apply {
                connectTimeout = 10000
                timeout = 10000
                addHostKeyVerifier(PromiscuousVerifier())
                connect(ip)
                authPassword(User.username, User.password)
            }
            ssh = client
            session = ssh.startSession()
            shell = session.startShell()
            input = shell.inputStream
            output = shell.outputStream
            println(Result.success("Успешно"))
            Result.success("Успешно")
        }catch(e: Exception) {
            println("Не прошло!")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun send(cmd: String): Result<String> = withContext(Dispatchers.IO){
        try{
            // Очищаем буфер перед отправкой
            clearInputBuffer()

            // Отправляем команду
            output.write(("$cmd\n").toByteArray())
            output.flush()

            // Ждем ответ с промптом
            val result = waitForPrompt(20000)
            Result.success(result)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    private suspend fun waitForPrompt(timeoutMs: Long): String = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val buffer = StringBuilder()
        val readBuffer = ByteArray(128)

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            val available = input.available()
            if (available > 0) {
                val bytesRead = input.read(readBuffer, 0, minOf(available, readBuffer.size))
                val chunk = String(readBuffer, 0, bytesRead)
                buffer.append(chunk)
                val spaceNext = buffer.toString()
                if(spaceNext.contains("--More--") || spaceNext.contains("More: <space>")){
                    output.write(" ".toByteArray())
                    output.flush()
                    val text = spaceNext.replace("--More--", "")
                    buffer.clear()
                    buffer.append(text)
                }
                // Проверяем наличие промпта Cisco
                if (hasCiscoPrompt(buffer.toString())) {
                    break
                }
            }
            delay(100)
        }
        return@withContext buffer.toString() 
    }

        private fun hasCiscoPrompt(text: String): Boolean {
            val patterns = listOf(
                ".*[>#]\\s*$".toRegex(),
                ".*Password:\\s*$".toRegex()
            )
            return patterns.any { it.containsMatchIn(text) }
        }

    private suspend fun clearInputBuffer() = withContext(Dispatchers.IO) {
        try {
            while (input.available() > 0) {
                input.skip(input.available().toLong())
            }
        } catch (_: Exception) { }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO){
        try{shell.close()
            println("Shell Закрыто успешно!")
        } catch (_: Exception){}
        try{
            session.close()
            println("Session Закрыто успешно!")
        } catch (_: Exception){}
        try{
            ssh.disconnect()
            println("SSH Закрыто успешно!")
        } catch (_: Exception){}
    }



    /* suspend fun send(cmd: String): Result<String> = withContext(Dispatchers.IO){
         try{
             output.write((cmd + "\n").toByteArray())
             output.flush()
             println(Result.success("Успешноx2"))
             Result.success(withTimeout(20000){
                 IOUtils.readFully(input).toString(Charsets.UTF_8)
             })
         } catch (e: Exception){
             println("Не прошло!x2")
             Result.failure(e)
         }
     }*/
}