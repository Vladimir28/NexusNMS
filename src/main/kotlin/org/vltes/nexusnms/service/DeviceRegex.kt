package org.vltes.nexusnms.service

object DeviceRegex {
    fun ipAddress(ip: String): Boolean{
        val ipv4 = Regex("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\$")
        return ipv4.matches(ip)
    }
    fun hostName(hostname: String): String{
        val regex = Regex("""^hostname\s+(\S+)""", RegexOption.MULTILINE)
        val match = regex.find(hostname)
        return match?.groupValues?.get(1) ?: "—"
    }
    fun model(model: String): String{
        var regex = Regex("""^Cisco\b""", RegexOption.MULTILINE)
        if(regex.containsMatchIn(model)){
            regex = Regex("""Model number\s*:\s*(\S+)""")
            val match = regex.find(model)
            return "Cisco ${match?.groupValues?.get(1) ?: "—"}"
        }
        else{
            return "—"
        }
    }
}