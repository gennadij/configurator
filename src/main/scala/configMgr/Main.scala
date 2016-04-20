package configMgr

/**
  * TODO for whole project
  * definition von dem letztem Step
  * multichoose bei der Auswahl der Componenten
  * pruefe ob mehrer Stepps als firstStep definiert sind
  *
  *
  */
object Main {
  def main(args : Array[String]) = {
    println("I am generic configurator")
    println("Generic configurator started")

    //TODO create Factory object
    val configMgr = new ConfigMgr

    val container = configMgr.loadStepsFromXML
    val currentConfig = configMgr.loadCurrentConfig
    
    
    val firstStep = configMgr.getFirstStep(container)
    currentConfig.steps += firstStep
    
    println("First Step => id: " + firstStep.id)
    println("Components for first step2")
    for(components <- firstStep.components){
      println(components.nameToShow)
    }
    
     def nextStep(enter: String): Unit = {
      val  nextStep = configMgr.getNextStep(container, enter)
       currentConfig.steps += nextStep
      println("Next Step: " + nextStep.nameToShow)
      for(components <- nextStep.components){
        println("Components: " + components.nameToShow)
      }
    }
     
    while(true){
      println("enter a id for component: ")

      val enter = scala.io.StdIn.readLine()
      
      enter match {
        case "q" => System.exit(0)
        case a if a.length == 6 => nextStep(enter)
        case _ => println("no enter")
      }
    }
  }
}