import sbt._

class SmemcacheProject(info: ProjectInfo) extends DefaultProject(info) {
  val codaRepo      = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val specsRepo     = "Specs Repo" at "http://nexus.scala-tools.org/content/repositories/snapshots"
  val specs         = "org.scala-tools.testing" % "specs_2.8.0.RC3" % "1.6.5-SNAPSHOT"
  val mockito       = "org.mockito" % "mockito" % "1.8.4" from "http://mockito.googlecode.com/files/mockito-all-1.8.4.jar"
  val spymemcached  = "net.spy.memcached" % "spymemcached" % "2.4.2" from "http://spymemcached.googlecode.com/files/memcached-2.4.2.jar"
  val snapshotsRepo = "Scala Tools Snapshots Repository" at "http://scala-tools.org/repo-snapshots/"
  val logula        = "com.codahale" %% "logula" % "1.0.1" withSources()
}
