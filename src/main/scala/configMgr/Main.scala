package configMgr

//import org.json4s._
//import org.json4s.jackson.JsonMethods._
object Main {
  def main(args : Array[String]) = {
    println("I am generic configurator")
    println("Generic configurator started")

    //TODO create Factory object
    val configMgr = new ConfigMgr

//    for(step <- configMgr.loadStepsFromXML) {
//      println("Die Steps: " + step)
//      println("Die Components von Step: " + step.components)
//      println("############################################")
//    }

//    println("First Step => id: " + configMgr.getFirstStep.id)

    println("Components for selecting")

    for(component <- configMgr.getComponentsForStep("001")){
      println("Name: " + component.nameToShow)
      println("Id: " + component.id)
    }

    println("enter a id for component: ")

//    val idForSelectedComponent = scala.io.StdIn.readLine()

    println("Next Step => id: " )
     
  }
}