package models.tests

object CurrentConfig {
  
  val currentConfig: CurrentConfig = new CurrentConfig
  
  
  def printClassHash = {
    currentConfig.printClassHash
  }
}

class CurrentConfig {
  
  var step: Step = Step("", List())
  
  val id: String = "CurrentConfig"
  
  def printClassHash = {
    println(CurrentConfig.getClass.hashCode())
  }
  
  def addFirstStep(step: Step) = {
    this.step = step
  }
  
  def addComponent(step: Step, component: Component): Unit = {
    getStep(step).components.::(component)
  }
  
  def addStep(step: Step, component: Component) = {
    
    getComponent(component).head.step.copy(step.id, step.components)
  }
  
  def getCurrentConfig = this.step
  
  private def getStep(stepB: Step): Step = {
    
    val stepA = this.step
    
    getStepRecursive(stepA, stepB)
  }
  
  private def getStepRecursive(stepA: Step, stepB: Step): Step = {
    if(stepA.id == stepB.id){
      stepA
    }else{
      getStepRecursive(stepA.components.head.step, stepB)
    }
  }
  
  private def getComponent(componentB: Component) = {
    val componentA = this.step.components
    
    getComponentRecursive(componentA, componentB)
  }
  
  private def getComponentRecursive(componentsA: List[Component], componentB: Component): List[Component] = {
    if(componentsA.contains(componentB)) {
      componentsA
    }else{
      getComponentRecursive(componentsA.head.step.components, componentB)
    }
  }
  
}

case class Step(
    id: String,
    components: List[Component]
)

case class Component(
   it: String,
   step: Step
)