package com.protose.smemcache.test
import org.specs._
import org.specs.mock.Mockito
import org.mockito._
import java.net.InetSocketAddress
import com.protose.smemcache.Smemcache._

object SmemcacheSpec extends Specification with Mockito {
    "implicitly converting a string to InetSocketAddress" should {
        "split the host and port" in {
            val addr: InetSocketAddress = "localhost:1234"
            addr.getHostName must_== "localhost"
            addr.getPort must_== 1234
        }

        "set the port to 11211 if its not in the string" in {
            val addr: InetSocketAddress = "localhost"
            addr.getHostName must_== "localhost"
            addr.getPort must_== 11211
        }
    }
}

// vim: set ts=4 sw=4 et:
