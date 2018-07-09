package org.tests

// NextStep wird bei der Step gehalt, aber das nur bei der CurrentConfig.

object CurrentConfig {
  
  val currentConfig: CurrentConfig = new CurrentConfig
  
  
  def printClassHash = {
    currentConfig.printClassHash
  }
  
  def addStep(nextStep: Option[Step], fatherStep: Option[Step]) = {
    currentConfig.addStep(nextStep, fatherStep)
  }
  
  def addComponent(step: Step, component: Component): Unit = {
    currentConfig.addComponent(step, component)
  }
  
  def printCurrentConfig = {
    currentConfig.printCurrentConfig
  }
}

class CurrentConfig {
  
  var step: Option[Step] = None
  
  val id: String = "CurrentConfig"
  
  def printClassHash = {
    println(CurrentConfig.getClass.hashCode())
  }
  
//  def addFirstStep(step: Step) = {
//    this.step = step
//  }
  
  private def addComponent(step: Step, component: Component): Unit = {
    step.components.::(component)
  }
  
  private def addStep(nextStep: Option[Step], fatherStep: Option[Step]) = {
    
//    getComponent(component).head.step.copy(step.id, step.components)
    
    fatherStep match {
      case Some(fatherStep) => fatherStep.nextStep = nextStep
      case None => step = nextStep
    }
  }
  
  
  
  
  def getCurrentConfig = this.step
  
  def printCurrentConfig = {
    getNextStep(this.step)
  }
  
  private def getNextStep(step: Option[Step]): Unit = {
    
    step.get.nextStep match {
      case Some(step) => getNextStep(Some(step))
      case None => {
        println(this.step.get.id)
        this.step.get.components foreach {component => println(component.id)}
      }
    }
  }
  
  private def getStep(stepB: Step): Step = {
    
//    val stepA = this.step
//    
//    getStepRecursive(stepA, stepB)
    ???
  }
  
  private def getStepRecursive(stepA: Step, stepB: Step): Step = {
//    if(stepA.id == stepB.id){
//      stepA
//    }else{
//      getStepRecursive(stepA.components.head.step, stepB)
//    }
    
    ???
  }
  
  private def getComponent(componentB: Component) = {
//    val componentA = this.step.components
//    
//    getComponentRecursive(componentA, componentB)
  ???
  }
  
  private def getComponentRecursive(componentsA: List[Component], componentB: Component): List[Component] = {
//    if(componentsA.contains(componentB)) {
//      componentsA
//    }else{
//      getComponentRecursive(componentsA.head.step.components, componentB)
//    }
    
    ???
  }
  
}

case class Step(
    var id: String,
    var components: List[Component],
    var nextStep: Option[Step]
)

case class Component(
   id: String
)