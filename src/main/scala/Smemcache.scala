package com.protose.smemcache
import java.net.InetSocketAddress

object Smemcache {
    implicit def string2InetSocketAddress(string: String):
        InetSocketAddress = {
        val split    = string.split(":")
        split.length match {
            case 1 => new InetSocketAddress(split.first, 11211)
            case 2 => new InetSocketAddress(split.first, split.last.toInt)
        }
    }
}
class Smemcache {
}

// vim: set ts=4 sw=4 et:
