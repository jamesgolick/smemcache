package smemcache

import java.net.InetSocketAddress
import java.util.concurrent.Future
import java.lang.{Boolean => JBool}

import net.spy.memcached.MemcachedClient

import scala.collection.JavaConversions._

object Memcache {
  implicit def string2InetSocketAddress(string: String):
      InetSocketAddress = {
      val split    = string.split(":")
      split.length match {
          case 1 => new InetSocketAddress(split.head, 11211)
          case 2 => new InetSocketAddress(split.head, split.last.toInt)
      }
  }

  def apply(servers: InetSocketAddress*) = {
      val client = new MemcachedClient(servers: _*)
      new Memcache(client)
  }
}

class Memcache(val client: MemcachedClient) {
  def get[A](key: String): Option[A] = {
    client.get(key) match {
      case a: A => Some(a)
      case null => None
    }
  }

  def set[A](key: String, value: A, expiration: Int = 0): Future[JBool] = {
    client.set(key, expiration, value.asInstanceOf[Object])
  }

  def multiget[A](keys: Set[String]): Map[String, A] = {
    client.getBulk(keys.toList).foldLeft(Map[String, A]()) { case (m, (k,v)) =>
      m + (k -> v.asInstanceOf[A])
    }
  }

  def multigetWithMiss[A](keys: Set[String])(missFunc: String => Option[A]): Map[String, A] = {
    val hits = multiget(keys)
    (keys -- hits.keySet).foldLeft[Map[String, A]](hits) { (results, missKey) =>
      missFunc(missKey) match {
        case Some(missValue) =>
          set(missKey, missValue)
          results + (missKey -> missValue)
        case None => results
      }
    }
  }

  def multigetWithBulkMiss[A](keys: Set[String])(missFunc: Set[String] => Map[String, A]): Map[String, A] = {
    val hits = multiget(keys)
    val misses = missFunc(keys -- hits.keySet)
    misses.foreach { case (key, value) => set(key, value) }
    hits ++ misses
  }

  def prepend[A](key: String, value: A): Future[JBool] = {
    client.prepend(0, key, value)
  }

  def delete(key: String): Future[JBool] = {
    client.delete(key)
  }

  def add[A](key: String, value: A, expiration: Int = 0): Future[JBool] = {
    client.add(key, expiration, value.asInstanceOf[Object])
  }

  def incr(key: String, by: Int = 1): Long = {
    client.incr(key, by)
  }

  def decr(key: String, by: Int = 1): Long = {
    client.decr(key, by)
  }
}
