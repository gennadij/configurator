package org.main

import org.configMgr._
import org.configTree.component._
import org.configTree.step._

class TestScenario2 {
  def scenario2 = {
    
    println("############################## SCENARIO 2 #######################")
    
    startConfig
    
    step1
    
    step2
    
    step3
    
    step4
    
    step4
    
    step5
  
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
    
    def step1 = {
      val selection = Set(new SelectedComponent("001003"))
      
      val step = ConfigMgr.getNextStep(selection)
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      require(step.id == "003", "id must be 003")
      
      require(step.selectionCriterium.min == "2")
      require(step.selectionCriterium.max == "4")
  
      require(step.nextStep(0).byComponent == "003001")
      require(step.nextStep(0).step == "004")
      require(step.nextStep(1).byComponent == "003002")
      require(step.nextStep(1).step == "004")
      require(step.nextStep(2).byComponent == "003003")
      require(step.nextStep(2).step == "004")
      require(step.nextStep(3).byComponent == "003004")
      require(step.nextStep(3).step == "004")
      
      
      require(step.components(0).id == "003001")
      require(step.components(0).nameToShow == "component 003001")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "003002")
      require(step.components(1).nameToShow == "component 003002")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "003003")
      require(step.components(2).nameToShow == "component 003003")
      require(step.components(2).isInstanceOf[MutableComponent] == true)
      require(step.components(2).minValue == 0)
      require(step.components(2).maxValue == 10)
      require(step.components(2).defaultValue == 5)
      require(step.components(2).interval == 1)
      require(step.components(2).intervals == List.empty)
      
      require(step.components(3).id == "003004")
      require(step.components(3).nameToShow == "component 003004")
      require(step.components(3).isInstanceOf[MutableComponent] == true)
      require(step.components(3).minValue == 0)
      require(step.components(3).maxValue == 10)
      require(step.components(3).defaultValue == 5)
      require(step.components(3).interval == 1)
      require(step.components(3).intervals == List.empty)
      
      require(ConfigMgr.currentConfig.size == 1, "CurrentConfig size is 1")
      require(ConfigMgr.currentConfig(0).id == "001")
      require(ConfigMgr.currentConfig(0).nameToShow == "step 001")
      require(ConfigMgr.currentConfig(0).components.size == 1)
      require(ConfigMgr.currentConfig(0).components(0).id == "001003")
      require(ConfigMgr.currentConfig(0).components(0).nameToShow == "component 001003")
    }
    
    def step2 = {
      
      val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003003", 4),
                                             new SelectedComponent("003004", 5)))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      
      require(step.id == "004", step.id)
      
      require(step.selectionCriterium.min == "1")
      require(step.selectionCriterium.max == "2")
  
      require(step.nextStep(0).byComponent == "004001")
      require(step.nextStep(0).step == "005")
      require(step.nextStep(1).byComponent == "004002")
      require(step.nextStep(1).step == "005")
      require(step.nextStep(2).byComponent == "004003")
      require(step.nextStep(2).step == "006")
      
      require(step.components(0).id == "004001")
      require(step.components(0).nameToShow == "component 004001")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "004002")
      require(step.components(1).nameToShow == "component 004002")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "004003")
      require(step.components(2).nameToShow == "component 004003")
      require(step.components(2).isInstanceOf[ImmutableComponent] == true)
      
      require(ConfigMgr.currentConfig.size == 2, "CurrentConfig size is 2")
      require(ConfigMgr.currentConfig(1).id == "003")
      require(ConfigMgr.currentConfig(1).nameToShow == "step 003")
      require(ConfigMgr.currentConfig(1).components.size == 2)
      require(ConfigMgr.currentConfig(1).components(0).isInstanceOf[CurrentConfigMutableComponent] == true)
      require(ConfigMgr.currentConfig(1).components(0).id == "003003")
      require(ConfigMgr.currentConfig(1).components(0).nameToShow == "component 003003")
      require(ConfigMgr.currentConfig(1).components(0).value == 4)
      require(ConfigMgr.currentConfig(1).components(1).isInstanceOf[CurrentConfigMutableComponent] == true)
      require(ConfigMgr.currentConfig(1).components(1).id == "003004")
      require(ConfigMgr.currentConfig(1).components(1).nameToShow == "component 003004")
      require(ConfigMgr.currentConfig(1).components(1).value == 5)
    }
    //TODO
    def step3 = {
      val selection = new SelectedComponent("004002")
      
      val step= ConfigMgr.getNextStep(Set(selection))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      
      require(step.id == "005")
      
      require(step.selectionCriterium.min == "1")
      require(step.selectionCriterium.max == "1")
  
      require(step.nextStep(0).byComponent == "005001")
      require(step.nextStep(0).step == "007")
      require(step.nextStep(1).byComponent == "005002")
      require(step.nextStep(1).step == "007")
      require(step.nextStep(2).byComponent == "005003")
      require(step.nextStep(2).step == "007")
      require(step.nextStep(3).byComponent == "005004")
      require(step.nextStep(3).step == "008")
      require(step.nextStep(4).byComponent == "005005")
      require(step.nextStep(4).step == "008")
      
      require(step.components(0).id == "005001")
      require(step.components(0).nameToShow == "component 005001")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "005002")
      require(step.components(1).nameToShow == "component 005002")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "005003")
      require(step.components(2).nameToShow == "component 005003")
      require(step.components(2).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(3).id == "005004")
      require(step.components(3).nameToShow == "component 005004")
      require(step.components(3).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(4).id == "005005")
      require(step.components(4).nameToShow == "component 005005")
      require(step.components(4).isInstanceOf[ImmutableComponent] == true)
  
      
      require(ConfigMgr.currentConfig.size == 3, "CurrentConfig size is 3")
      require(ConfigMgr.currentConfig(2).id == "004")
      require(ConfigMgr.currentConfig(2).nameToShow == "step 004")
      require(ConfigMgr.currentConfig(2).components.size == 1)
       require(ConfigMgr.currentConfig(2).components(0).isInstanceOf[CurrentConfigImmutableComponent] == true)
      require(ConfigMgr.currentConfig(2).components(0).id == "004002")
      require(ConfigMgr.currentConfig(2).components(0).nameToShow == "component 004002")
    }
    
    def step4 = {
      val selection = new SelectedComponent("005005")
      
      val step = ConfigMgr.getNextStep(Set(selection))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[LastStep] == true)
      
      require(step.id == "008")
      
      require(step.selectionCriterium.min == "1")
      require(step.selectionCriterium.max == "1")
  
      require(step.nextStep(0).byComponent == "008001")
      require(step.nextStep(0).step == "000")
      require(step.nextStep(1).byComponent == "008002")
      require(step.nextStep(1).step == "000")
      
      require(step.components(0).id == "008001")
      require(step.components(0).nameToShow == "component 008001")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "008002")
      require(step.components(1).nameToShow == "component 008002")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(ConfigMgr.currentConfig.size == 4, "CurrentConfig size is 4")
      require(ConfigMgr.currentConfig(3).id == "005")
      require(ConfigMgr.currentConfig(3).nameToShow == "step 005")
      require(ConfigMgr.currentConfig(3).components.size == 1)
      require(ConfigMgr.currentConfig(3).components(0).id == "005005")
      require(ConfigMgr.currentConfig(3).components(0).nameToShow == "component 005005")
    }
    
    def step5 = {
      
      val selection = new SelectedComponent("008002")
      
      val step = ConfigMgr.getNextStep(Set(selection))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[FinalStep] == true)
      
      require(step.id == "0")
      
      require(ConfigMgr.currentConfig.size == 5, "CurrentConfig size is 5")
      require(ConfigMgr.currentConfig(4).id == "008")
      require(ConfigMgr.currentConfig(4).nameToShow == "step 008")
      require(ConfigMgr.currentConfig(4).components.size == 1)
      require(ConfigMgr.currentConfig(4).components(0).id == "008002")
      require(ConfigMgr.currentConfig(4).components(0).nameToShow == "component 008002")
    }
  }
}