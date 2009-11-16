package com.protose.smemcache.test
import org.specs._
import org.specs.mock.Mockito
import org.mockito._
import java.net.InetSocketAddress
import com.protose.smemcache.Memcache._
import net.spy.memcached.MemcachedClient
import java.util.concurrent.FutureTask
import java.util.concurrent.Callable
import java.lang.{Boolean => JBool}

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

    val underlyingClient = mock[MemcachedClient]
    val cache            = new Memcache(underlyingClient)

    "doing a cache get" should {
        "delegate to the underlying client" in {
            underlyingClient.get("someKey") returns "some value"
            cache.get("someKey") must_== "some value"
            underlyingClient.get("someKey") was called
        }
    }

    val callable = new Callable[JBool]() {
        def call() = new JBool(true)
    }
    val future = new FutureTask[java.lang.Boolean](callable)

    "doing a set" should {
        "set a default expiry in the client" in {
            underlyingClient.set("someKey", 0, "someValue") returns future
            cache.set("someKey", "someValue")
            underlyingClient.set("someKey", 0, "someValue") was called
        }
    }
}

// vim: set ts=4 sw=4 et:
