package org.vltes.nexusnms.service

import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeText
class Terminal {
    fun openTerminalSsh(
        host: String,
        user: String,
        password: String,
        port: Int = 22,
        extraCommandAfterLogin: String? = null // например "show running-config"
    ) {
        val os = System.getProperty("os.name").lowercase()
        // Создаём expect-скрипт в temp (mac/linux)
        fun createExpectScript(): File {
            val script = Files.createTempFile("ssh_expect_", ".exp").toFile()
            // escape двойных кавычек/долларов в командной строчке, если нужно
            val safeExtra = extraCommandAfterLogin?.replace("\"", "\\\"")?.replace("$", "\\$")
            val expectContent = buildString {
                appendLine("#!/usr/bin/expect -f")
                appendLine("trap {file delete -force [info script]; exit} {SIGINT SIGTERM SIGHUP}")
                appendLine("set timeout -1")
                appendLine("log_user 0") // вывод в терминал
                append("spawn ssh $user@$host")
                appendLine()
                appendLine("expect {")
                appendLine("""  -re ".*assword.*" {""")
                appendLine("""    send "$password\r"""")
                appendLine("log_user 1")
                appendLine("  }")
                appendLine("  eof {}")
                appendLine("}")
                if (!safeExtra.isNullOrBlank()) {
                    // даём некоторое ожидание и отправляем команду
                    appendLine("""expect { -re ".*#|.*\$|.*>" { send "$safeExtra\r" } timeout {}}""")
                }
                appendLine("expect {")
                appendLine("""  -re {.*#|.*\$|.*>} {file delete -force [info script]}}""")
                // Оставляем сессию интерактивной
                appendLine("interact")
                appendLine("file delete -force [info script]")
            }
            script.writeText(expectContent)
            script.setExecutable(true)
            return script
        }

        try {
            when {
                os.contains("win") -> {
                    // Windows: используем plink (из пакета PuTTY). plink.exe должен быть в PATH или указать полный путь.
                    // plink поддерживает -pw <password>. (Опять же: небезопасно хранить пароль в аргументах)
                    // Если plink нет — рекомендую настроить ключи или использовать Windows OpenSSH с Pageant / ключами.
                    val plink = "plink" // или "C:\\path\\to\\plink.exe"
                    val baseArgs = mutableListOf(plink, "-ssh", "-P", port.toString(), "-pw", password, "$user@$host")
                    if (!extraCommandAfterLogin.isNullOrBlank()) {
                        baseArgs.add(extraCommandAfterLogin)
                    }
                    // Откроем cmd и оставим окно открытым (/k)
                    val cmd = listOf("cmd.exe", "/k") + baseArgs
                    ProcessBuilder(cmd).inheritIO().start()
                }

                os.contains("mac") -> {
                    // macOS: создаём expect-скрипт и запускаем его через Terminal.app (через osascript)
                    val script = createExpectScript()
                    // AppleScript: tell application "Terminal" to do script "expect '/tmp/...' ; exec bash"
                    val appleCommand = """
                        tell application "Terminal" 
                            activate 
                            do script "expect '${script.absolutePath}'" 
                            end tell
                            """.trimIndent()
                    val pb = ProcessBuilder("osascript", "-e", appleCommand)
                    val process = pb.start()
                    //process.onExit().thenRun { script.delete() }

                }

                /*os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                // Linux/RedOS: создаём expect-скрипт и запускаем его в доступном терминале
                val script = createExpectScript()
                val terminalCommand = when {
                    File("/usr/bin/gnome-terminal").exists() -> listOf("gnome-terminal", "--", "bash", "-lc", "expect '${script.absolutePath}'; exec bash")
                    File("/usr/bin/konsole").exists() -> listOf("konsole", "-e", "bash", "-lc", "expect '${script.absolutePath}'; exec bash")
                    File("/usr/bin/xterm").exists() -> listOf("xterm", "-e", "bash", "-lc", "expect '${script.absolutePath}'; exec bash")
                    else -> listOf("sh", "-c", "expect '${script.absolutePath}'; exec $SHELL") // fallback (может работать не в GUI)
                }
                ProcessBuilder(terminalCommand).inheritIO().start()
            }*/

                else -> throw UnsupportedOperationException("Неизвестная ОС: $os")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Можно показать пользователю диалог об ошибке
        }
    }
}