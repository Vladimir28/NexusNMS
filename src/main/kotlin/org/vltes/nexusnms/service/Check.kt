package org.vltes.nexusnms.service

object Check {
    fun ipAddress(ip: String): Boolean{
        val ipv4 = Regex("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\$")
        return ipv4.matches(ip)
    }
}