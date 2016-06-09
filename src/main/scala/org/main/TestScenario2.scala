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
    
    step6
  
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
      
      val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003003, 4"),
                                             new SelectedComponent("003004, 5")))
      
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
      require(ConfigMgr.currentConfig(1).id == "002")
      require(ConfigMgr.currentConfig(1).nameToShow == "step 002")
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
      val selected003001 = new SelectedComponent("003001")
      val selected003002 = new SelectedComponent("003002")
      
      val fourthStep = ConfigMgr.getNextStep(Set(selected003001, selected003002))
      
      require(fourthStep.isInstanceOf[ErrorStep] != true, fourthStep.errorMessage)
  
      require(fourthStep.isInstanceOf[DefaultStep] == true)
      
      require(fourthStep.id == "004", fourthStep.id)
      
      require(fourthStep.selectionCriterium.min == "1")
      require(fourthStep.selectionCriterium.max == "2")
  
      require(fourthStep.nextStep(0).byComponent == "004001")
      require(fourthStep.nextStep(0).step == "005")
      require(fourthStep.nextStep(1).byComponent == "004002")
      require(fourthStep.nextStep(1).step == "005")
      require(fourthStep.nextStep(2).byComponent == "004003")
      require(fourthStep.nextStep(2).step == "006")
      
      require(fourthStep.components(0).id == "004001")
      require(fourthStep.components(0).nameToShow == "component 004001")
      require(fourthStep.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(fourthStep.components(1).id == "004002")
      require(fourthStep.components(1).nameToShow == "component 004002")
      require(fourthStep.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(fourthStep.components(2).id == "004003")
      require(fourthStep.components(2).nameToShow == "component 004003")
      require(fourthStep.components(2).isInstanceOf[ImmutableComponent] == true)
  
      
      require(ConfigMgr.currentConfig.size == 3, "CurrentConfig size is 3")
      require(ConfigMgr.currentConfig(2).id == "003")
      require(ConfigMgr.currentConfig(2).nameToShow == "step 003")
      require(ConfigMgr.currentConfig(2).components.size == 2)
      require(ConfigMgr.currentConfig(2).components(0).id == "003001")
      require(ConfigMgr.currentConfig(2).components(0).nameToShow == "component 003001")
      require(ConfigMgr.currentConfig(2).components(1).id == "003002")
      require(ConfigMgr.currentConfig(2).components(1).nameToShow == "component 003002")
    }
    
    def step4 = {
      val selected004001 = new SelectedComponent("004001")
      
      val fifthStep = ConfigMgr.getNextStep(Set(selected004001))
      
      require(fifthStep.isInstanceOf[ErrorStep] != true, fifthStep.errorMessage)
  
      require(fifthStep.isInstanceOf[DefaultStep] == true)
      
      require(fifthStep.id == "005")
      
      require(fifthStep.selectionCriterium.min == "1")
      require(fifthStep.selectionCriterium.max == "1")
  
      require(fifthStep.nextStep(0).byComponent == "005001")
      require(fifthStep.nextStep(0).step == "007")
      require(fifthStep.nextStep(1).byComponent == "005002")
      require(fifthStep.nextStep(1).step == "007")
      require(fifthStep.nextStep(2).byComponent == "005003")
      require(fifthStep.nextStep(2).step == "007")
      require(fifthStep.nextStep(3).byComponent == "005004")
      require(fifthStep.nextStep(3).step == "008")
      require(fifthStep.nextStep(4).byComponent == "005005")
      require(fifthStep.nextStep(4).step == "008")
      
      require(fifthStep.components(0).id == "005001")
      require(fifthStep.components(0).nameToShow == "component 005001")
      require(fifthStep.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(fifthStep.components(1).id == "005002")
      require(fifthStep.components(1).nameToShow == "component 005002")
      require(fifthStep.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(fifthStep.components(2).id == "005003")
      require(fifthStep.components(2).nameToShow == "component 005003")
      require(fifthStep.components(2).isInstanceOf[ImmutableComponent] == true)
      
      require(fifthStep.components(3).id == "005004")
      require(fifthStep.components(3).nameToShow == "component 005004")
      require(fifthStep.components(3).isInstanceOf[ImmutableComponent] == true)
      
      require(fifthStep.components(4).id == "005005")
      require(fifthStep.components(4).nameToShow == "component 005005")
      require(fifthStep.components(4).isInstanceOf[ImmutableComponent] == true)
  
      
      require(ConfigMgr.currentConfig.size == 4, "CurrentConfig size is 4")
      require(ConfigMgr.currentConfig(3).id == "004")
      require(ConfigMgr.currentConfig(3).nameToShow == "step 004")
      require(ConfigMgr.currentConfig(3).components.size == 1)
      require(ConfigMgr.currentConfig(3).components(0).id == "004001")
      require(ConfigMgr.currentConfig(3).components(0).nameToShow == "component 004001")
    }
    
    def step5 = {
      
      val selected005001 = new SelectedComponent("005001")
      
      val seventhStep = ConfigMgr.getNextStep(Set(selected005001))
      
      require(seventhStep.isInstanceOf[ErrorStep] != true, seventhStep.errorMessage)
  
      require(seventhStep.isInstanceOf[LastStep] == true)
      
      require(seventhStep.id == "007")
      
      require(seventhStep.selectionCriterium.min == "1")
      require(seventhStep.selectionCriterium.max == "1")
  
      require(seventhStep.nextStep(0).byComponent == "007001")
      require(seventhStep.nextStep(0).step == "000")
      require(seventhStep.nextStep(1).byComponent == "007002")
      require(seventhStep.nextStep(1).step == "000")
      require(seventhStep.nextStep(2).byComponent == "007003")
      require(seventhStep.nextStep(2).step == "000")
      
      require(seventhStep.components(0).id == "007001")
      require(seventhStep.components(0).nameToShow == "component 007001")
      require(seventhStep.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(seventhStep.components(1).id == "007002")
      require(seventhStep.components(1).nameToShow == "component 007002")
      require(seventhStep.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(seventhStep.components(2).id == "007003")
      require(seventhStep.components(2).nameToShow == "component 007003")
      require(seventhStep.components(2).isInstanceOf[ImmutableComponent] == true)
      
      require(ConfigMgr.currentConfig.size == 5, "CurrentConfig size is 5")
      require(ConfigMgr.currentConfig(4).id == "005")
      require(ConfigMgr.currentConfig(4).nameToShow == "step 005")
      require(ConfigMgr.currentConfig(4).components.size == 1)
      require(ConfigMgr.currentConfig(4).components(0).id == "005001")
      require(ConfigMgr.currentConfig(4).components(0).nameToShow == "component 005001")
    }
    
    def step6 = {
      
      val selected007001 = new SelectedComponent("007001")
      
      val eighthStep = ConfigMgr.getNextStep(Set(selected007001))
      
      require(eighthStep.isInstanceOf[ErrorStep] != true, eighthStep.errorMessage)
  
      require(eighthStep.isInstanceOf[FinalStep] == true)
      
      require(eighthStep.id == "0")
      
      require(ConfigMgr.currentConfig.size == 6, "CurrentConfig size is 6")
      require(ConfigMgr.currentConfig(5).id == "007")
      require(ConfigMgr.currentConfig(5).nameToShow == "step 007")
      require(ConfigMgr.currentConfig(5).components.size == 1)
      require(ConfigMgr.currentConfig(5).components(0).id == "007001")
      require(ConfigMgr.currentConfig(5).components(0).nameToShow == "component 007001")
    }
  }
}