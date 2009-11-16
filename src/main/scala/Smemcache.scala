package com.protose.smemcache
import java.net.InetSocketAddress
import net.spy.memcached.MemcachedClient

object Memcache {
    implicit def string2InetSocketAddress(string: String):
        InetSocketAddress = {
        val split    = string.split(":")
        split.length match {
            case 1 => new InetSocketAddress(split.first, 11211)
            case 2 => new InetSocketAddress(split.first, split.last.toInt)
        }
    }
}

class Memcache(val client: MemcachedClient) {
    def get(key: String): Any = {
        client.get(key)
    }
}

// vim: set ts=4 sw=4 et:
