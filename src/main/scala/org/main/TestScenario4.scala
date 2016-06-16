package org.main

import org.configTree.component._
import org.configMgr._
import org.configTree.step._

class TestScenario4 {
  
  def scenario4 = {
    
    println("############################## SCENARIO 4 #######################")
    
    startConfig
    
    error1
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
      require(step.errorMessage(0) == "minValue is smaller or maxValue is greater as definition in configSttings", step.errorMessage)
//      require(step.errorComponent(0).id == "7", step.errorComponent(0).id)
//      require(step.errorComponent(0).errorMessage == "")
      
  
//      require(step.isInstanceOf[DefaultStep] == true)
//      
//      require(step.id == "004", step.id)
//      
//      require(step.selectionCriterium.min == "1")
//      require(step.selectionCriterium.max == "2")
//  
//      require(step.nextStep(0).byComponent == "004001")
//      require(step.nextStep(0).step == "005")
//      require(step.nextStep(1).byComponent == "004002")
//      require(step.nextStep(1).step == "005")
//      require(step.nextStep(2).byComponent == "004003")
//      require(step.nextStep(2).step == "006")
//      
//      require(step.components(0).id == "004001")
//      require(step.components(0).nameToShow == "component 004001")
//      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
//      
//      require(step.components(1).id == "004002")
//      require(step.components(1).nameToShow == "component 004002")
//      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
//      
//      require(step.components(2).id == "004003")
//      require(step.components(2).nameToShow == "component 004003")
//      require(step.components(2).isInstanceOf[ImmutableComponent] == true)
//      
//      require(ConfigMgr.currentConfig.size == 2, "CurrentConfig size is 2")
//      require(ConfigMgr.currentConfig(1).id == "003")
//      require(ConfigMgr.currentConfig(1).nameToShow == "step 003")
//      require(ConfigMgr.currentConfig(1).components.size == 2)
//      require(ConfigMgr.currentConfig(1).components(0).isInstanceOf[CurrentConfigMutableComponent] == true)
//      require(ConfigMgr.currentConfig(1).components(0).id == "003003")
//      require(ConfigMgr.currentConfig(1).components(0).nameToShow == "component 003003")
//      require(ConfigMgr.currentConfig(1).components(0).value == 4)
//      require(ConfigMgr.currentConfig(1).components(1).isInstanceOf[CurrentConfigMutableComponent] == true)
//      require(ConfigMgr.currentConfig(1).components(1).id == "003004")
//      require(ConfigMgr.currentConfig(1).components(1).nameToShow == "component 003004")
//      require(ConfigMgr.currentConfig(1).components(1).value == 5)
  }
}