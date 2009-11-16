import sbt._

class SmemcacheProject(info: ProjectInfo) extends DefaultProject(info) {
    val specs        = "org.specs" % "specs" % "1.6.0" from "http://specs.googlecode.com/files/specs-1.6.0.jar"
    val mockito      = "org.mockito" % "mockito" % "1.8.0" from "http://mockito.googlecode.com/files/mockito-all-1.8.0.jar"
    val spymemcached = "net.spy.memcached" % "spymemcached" % "2.4.2" from "http://spymemcached.googlecode.com/files/memcached-2.4.2.jar"
}

// vim: set ts=4 sw=4 et:
