package com.protose.smemcache

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
          case 1 => new InetSocketAddress(split.first, 11211)
          case 2 => new InetSocketAddress(split.first, split.last.toInt)
      }
  }

  def apply(servers: InetSocketAddress*) = {
      val client = new MemcachedClient(servers: _*)
      new Memcache(client)
  }
}

class Memcache(val client: MemcachedClient) {
  def get[A](key: String): A = {
    client.get(key).asInstanceOf[A]
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

  def prepend[A](key: String, value: A): Future[JBool] = {
    client.prepend(0, key, value)
  }
}
