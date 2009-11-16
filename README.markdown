h1. Smemcache

Smemcache is a scala wrapper around spymemcached (http://code.google.com/p/spymemcached/). It only implements a few calls right now, with more to possibly come later.

I'm still pretty noobish to scala, so everything is probably wrong. Patches welcome.

To use it:

import com.protose.smemcache.Memcache
import com.protose.smemcache.Memcache._

val cache = Memcache("memcache1.myapp.com", "memcache2.myapp.com")
cache.set("x", "y")
cache.get("x") // this returns a java.util.concurrent.Future
cache.multiGet(List("x", "y", "z")) // returns Map[String, Object]

