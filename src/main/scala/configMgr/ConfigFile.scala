package configMgr

//import org.json4s._
//import org.json4s.jackson.JsonMethods._

class ConfigFile {

  def getXML = scala.xml.XML.loadFile("src/main/scala/xml_json/config.xml")

  def toStep(stepXML: scala.xml.Node): Step = {
    new Step(
      (stepXML \ "id").text,
      (stepXML \ "nameToShow").text,
      (stepXML \ "nextStep").text,
      (stepXML \ "isStartStep").text
    )
  }

  def toComponents(componentXML: scala.xml.Node): Component = {
    new Component(
      (componentXML \ "id").text,
      (componentXML \ "nameToShow").text
    )
  }

//  val step: scala.xml.NodeSeq  = getXML \ "step"
//
//  val stepsObject: Seq[Step] = step.map(s => getStep(s))



}