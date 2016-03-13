package configMgr

import org.json4s._
import org.json4s.jackson.JsonMethods._
object Main {
  def main(args : Array[String]) = {
println("I am configurator")
    val configFile = new ConfigFile
    configFile.getJson
}
}