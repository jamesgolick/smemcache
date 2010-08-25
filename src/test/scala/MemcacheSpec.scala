package smemcache.test

import smemcache.Memcache
import smemcache.Memcache._

import java.lang.{Boolean => JBool}
import java.net.InetSocketAddress
import java.util.concurrent.FutureTask
import java.util.concurrent.Callable

import net.spy.memcached.MemcachedClient

import org.specs.Specification
import org.specs.mock.Mockito
import org.mockito.Matchers._

object MemcacheSpec extends Specification with Mockito {
  implicit def convertScalaListToJavaList(aList:List[String]) =
      java.util.Arrays.asList(aList.toArray: _*)

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

  "doing a cache get delegates and returns Some if the value is present" in {
    underlyingClient.get("someKey") returns "some value"
    cache.get[String]("someKey") must_== Some("some value")
    there was one(underlyingClient).get("someKey")
  }

  "doing a cache get delegates and returns None if the value is null" in {
    underlyingClient.get("someKey") returns null
    cache.get[String]("someKey") must_== None
    there was one(underlyingClient).get("someKey")
  }

  val callable = new Callable[JBool]() {
      def call() = new JBool(true)
  }
  val future = new FutureTask[java.lang.Boolean](callable)

  "doing a set sets a default expiry in the client" in {
    underlyingClient.set("someKey", 0, "someValue") returns future
    cache.set("someKey", "someValue")
    got {
      underlyingClient.set("someKey", 0, "someValue")
    }
  }

  "doing a set has a signature that accepts an expiration" in {
    underlyingClient.set("someKey", 9, "someValue") returns future
    cache.set("someKey", "someValue", 9)
    got {
      underlyingClient.set("someKey", 9, "someValue")
    }
  }

  "doing a multiget delegates to the client" in {
    val map      = new java.util.HashMap[String, Object]()
    val scalaMap = Map("key1" -> "value1")

    map.put("key1", "value1")
    underlyingClient.getBulk(Set("key1", "key2").toList) returns map
    cache.multiget(Set("key1", "key2"))() must_== scalaMap
  }

  "doing a multiget with a miss function calls the function with each miss" in {
    val map      = new java.util.HashMap[String, Object]()
    val scalaMap = Map("key1" -> "value1", "key2" -> "a", "key3" -> "a")
    var called   = List[String]()
    val missFunc = { key: String => called = called :+ key; Some("a") }

    map.put("key1", "value1")
    underlyingClient.getBulk(Set("key1", "key2", "key3").toList) returns map
    cache.multigetWithMiss(Set("key1", "key2", "key3"))(missFunc) must_== scalaMap
    there was one(underlyingClient).set("key2", 0, "a")
    there was one(underlyingClient).set("key3", 0, "a")
    called must_== List("key2", "key3")
  }

  "doing a multiget with a bulk miss function calls the function for all misses" in {
    val map      = new java.util.HashMap[String, Object]()
    val scalaMap = Map("key1" -> "value1", "key2" -> "asdf", "key3" -> "bsdf")
    var called   = Set[String]()
    val missFunc = { key: Set[String] => called = key; Map("key2" -> "asdf", "key3" -> "bsdf") }

    map.put("key1", "value1")
    underlyingClient.getBulk(Set("key1", "key2", "key3").toList) returns map
    cache.multigetWithBulkMiss(Set("key1", "key2", "key3"))(missFunc) must_== scalaMap
    there was one(underlyingClient).set("key2", 0, "asdf")
    there was one(underlyingClient).set("key3", 0, "bsdf")
    called must_== Set("key2", "key3")
  }

  "prepending to a key delegates to the client" in {
    underlyingClient.prepend(0, "a", "b") returns future
    cache.prepend("a", "b")
    got {
      underlyingClient.prepend(0, "a", "b")
    }
  }

  "doing a delete delegates to the client" in {
    underlyingClient.delete("a") returns future
    cache.delete("a") must_== future
    got {
      underlyingClient.delete("a")
    }
  }
}
