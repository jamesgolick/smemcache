package com.protose.smemcache.test
import org.specs._
import org.specs.mock.Mockito
import org.mockito._
import java.net.InetSocketAddress
import com.protose.smemcache.Memcache._
import net.spy.memcached.MemcachedClient

object MemcacheSpec extends Specification with Mockito {
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

    "doing a cache get" should {
        "delegate to the underlying client" in {
            val underlyingClient = mock[MemcachedClient]
            val cache            = new Memcache(underlyingClient)

            underlyingClient.get("someKey") returns "some value"
            cache.get("someKey") must_== "some value"
            underlyingClient.get("someKey") was called
        }
    }
}

// vim: set ts=4 sw=4 et:
