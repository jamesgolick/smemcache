import sbt._

class SmemcacheProject(info: ProjectInfo) extends DefaultProject(info) {
  val codaRepo      = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val specsRepo     = "Specs Repo" at "http://nexus.scala-tools.org/content/repositories/snapshots"
  val spyRepo       = "Spy Repo" at "http://files.couchbase.com/maven2/"
  val specs         = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5"
  val mockito       = "org.mockito" % "mockito-all" % "1.8.5"
  val spymemcached  = "net.spy.memcached" % "spymemcached" % "2.6" 
  val snapshotsRepo = "Scala Tools Snapshots Repository" at "http://scala-tools.org/repo-snapshots/"
  val logula        = "com.codahale" %% "logula" % "1.0.3" withSources()
}
