package configMgr

//import org.json4s._
//import org.json4s.jackson.JsonMethods._

class ConfigFile {

  def getXML = scala.xml.XML.loadFile("src/main/scala/configMgr/config.xml")

  def getStep(s: scala.xml.Node): Step = {
    new Step(
      (s \ "id").text,
      (s \ "nameToShow").text,
      (s \ "nextStep").text,
      (s \ "isStartStep").text
    )
  }

  val step: scala.xml.NodeSeq  = getXML \ "step"

  val stepsObject: Seq[Step] = step.map(s => getStep(s))


  for(stepCount <- stepsObject) {
    println(stepCount)
  }
}