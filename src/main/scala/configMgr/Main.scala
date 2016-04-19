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
    val container = configMgr.loadStepsFromXML
    
    val firstStep = configMgr.getFirstStep(container)
    
    println("First Step => id: " + firstStep.id)
    println("Components for first step2")
    for(components <- configMgr.getComponentsForStep(firstStep)){
      println(components.nameToShow)
    }
    
    val flag = true
    
    while(flag){
      println("enter a id for component: ")

      val enter = scala.io.StdIn.readLine()
      
      val operation = enter match {
        case "q" => System.exit(0)
        case a if a.length == 6 => println("nextStep")
        case _ => println("no enter")
        
      }
      
      
//      val nextStep = configMgr.getNextStep(container, enter, firstStep)
//    
//      println("NextStep: " + nextStep)
//
//      println("Next Step => id: " )
    }
    
    
     
  }
}