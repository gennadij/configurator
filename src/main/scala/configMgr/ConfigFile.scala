package configMgr

//import org.json4s._
//import org.json4s.jackson.JsonMethods._

class ConfigFile {
  def getJson : Unit = {
//    val json_string = scala.io.Source.fromFile("src/main/scala/configMgr/config.json").getLines.mkString
//    println(json_string)
//    val jsonObject = parse("" + json_string + "")
//    val steps = jsonObject \ "steps"
//    println(steps)
    
    val xmlTest = scala.xml.XML.loadFile("src/main/scala/configMgr/config.xml")
    println(xmlTest)
//    println(jsonObject)
  }
}