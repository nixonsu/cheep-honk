package com.nixonsu.cheephonk.utils

import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.UnknownHostException

class DnsResolutionLogger {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun logDnsResolution() {
        try {
            val hostname = "petrolspy.com.au"

            // Log the DNS resolution
            val addresses = InetAddress.getAllByName(hostname)
            for (address in addresses) {
                log.info("Resolved IP for " + hostname + ": " + address.hostAddress)
            }
        } catch (e: UnknownHostException) {
            log.error("Failed to resolve host: " + e.message)
        }
    }
}
