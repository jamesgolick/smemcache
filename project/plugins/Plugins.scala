class Plugins(info: sbt.ProjectInfo) extends sbt.PluginDefinition(info) {
  val codasRepo       = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val sbtRsync        = "com.codahale" % "rsync-sbt" % "0.1.1"
}
