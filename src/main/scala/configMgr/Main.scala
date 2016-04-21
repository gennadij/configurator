package configMgr

/**
  * TODO for whole project
  * - definition von dem letztem Step
  * - multichoose bei der Auswahl der Componenten
  * - pruefe ob mehrer Stepps als firstStep definiert sind
  * - definition von dem variablen Components (Slide, variable Massen)
  * - in currentCinfigutration unter Step nur die ausgewählte Komponente speichern
  * - Logger einrichten
  * - letzte Step wird nicht in der CurrentConfig hinzugefügt
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
    
    println("First Step => id: " + firstStep.id)
    println("Components for first step2")
    for(components <- firstStep.components){
      println(components.nameToShow)
    }
    
    def nextStep(enter: String): Unit = {
      val  nextStep = configMgr.getNextStep(container, enter) match {
        case step: Step => {
          val currentStep = configMgr.getStepWithSelectedComponent(container, enter)
          currentConfig.steps += currentStep(0)
          println("Next Step: " + step.nameToShow)
          for(components <- step.components){
            println("Components: " + components.nameToShow)
          }
        }
        case string: String => {
          println(string)
          println("Aktuelle Konfiguration")
          for(step <- currentConfig.steps){
        	  println(step.nameToShow)
        	  for(comp <- step.components){
        	    println("Components: " + comp.nameToShow)
        	  }
          }
        }
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