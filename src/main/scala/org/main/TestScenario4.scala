package org.main

import org.configTree.component._
import org.configMgr._
import org.configTree.step._

class TestScenario4 {
  
  def scenario4 = {
    
    println("############################## SCENARIO 4 #######################")
    
    startConfig
    
    error1
    
    error2
    
    error3
    
    error4
  }
  
  def startConfig = {
        
        ConfigMgr.currentConfig.clear()
        val firstStep = ConfigMgr.startConfig
        
        require(firstStep.isInstanceOf[FirstStep] == true)
        require(firstStep.id == "001", "id must be 001")
        require(firstStep.selectionCriterium.max == "1")
        require(firstStep.selectionCriterium.min == "1")
        require(firstStep.nextStep(0).step == "002")
        require(firstStep.nextStep(0).byComponent == "001001")
        require(firstStep.nextStep(1).step == "002")
        require(firstStep.nextStep(1).byComponent == "001002")
        require(firstStep.nextStep(2).step == "003")
        require(firstStep.nextStep(2).byComponent == "001003")
        
        require(firstStep.components(0).id == "001001")
        require(firstStep.components(0).nameToShow == "component 001001")
        require(firstStep.components(0).isInstanceOf[ImmutableComponent] == true)
        
        require(firstStep.components(1).id == "001002")
        require(firstStep.components(1).nameToShow == "component 001002")
        require(firstStep.components(1).isInstanceOf[ImmutableComponent] == true)
        
        require(firstStep.components(2).id == "001003")
        require(firstStep.components(2).nameToShow == "component 001003")
        require(firstStep.components(2).isInstanceOf[ImmutableComponent] == true)
        
        require(ConfigMgr.currentConfig.size == 0, "CurrentConfig size is 0")
    }
  
  
  def error1 = {
      
      val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003003", -1),
                                             new SelectedComponent("003004", 12)))
      
      require(step.isInstanceOf[ErrorStep] == true, step.errorMessage)
      
      require(step.id == "7")
      require(step.errorMessage == "error", step.errorMessage)
      require(step.errorComponent.size == 2, step.errorComponent.size) 
      require(step.errorComponent(0).id == "7", step.errorComponent(0).id)
      require(step.errorComponent(0).errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
//      require(step.errorComponent(0).isInstanceOf[SuccessComponent] == true)
      require(step.errorComponent(1).id == "7", step.errorComponent(0).id)
      require(step.errorComponent(1).errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
  }
  
    def error2 = {
      
      val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003003", 2),
                                             new SelectedComponent("003004", 12)))
      
      require(step.isInstanceOf[ErrorStep] == true, step.errorMessage)
      
      require(step.id == "7")
      require(step.errorMessage == "error", step.errorMessage)
      require(step.errorComponent.size == 1, step.errorComponent.size)
      require(step.errorComponent(0).id == "7", step.errorComponent(0).id)
      require(step.errorComponent(0).errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
  }
    
  def error3 = {
    
    val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003003", -1),
                                           new SelectedComponent("003004", 9)))
    
    require(step.isInstanceOf[ErrorStep] == true, step.errorMessage)
    
    require(step.id == "7")
    require(step.errorMessage == "error", step.errorMessage)
    require(step.errorComponent.size == 1, step.errorComponent.size) 
    require(step.errorComponent.head.id == "7", step.errorComponent(0).id)
    require(step.errorComponent.head.errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
  }
  
  def error4 = {
    
    val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003001", 2),
                                           new SelectedComponent("003002	", 8)))
    
    require(step.isInstanceOf[ErrorStep] == true, step.toString())
    
    require(step.id == "7")
    require(step.errorMessage == "error", step.errorMessage)
    require(step.errorComponent.size == 1, step.errorComponent.size) 
    require(step.errorComponent.head.id == "7", step.errorComponent(0).id)
    require(step.errorComponent.head.errorMessage == "ImmutableComponent allowed not parameter for value", step.errorComponent.head.errorMessage)
  }
}