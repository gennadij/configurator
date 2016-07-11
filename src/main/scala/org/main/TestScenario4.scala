  package org.main

import org.configTree.component._
import org.configMgr._
import org.configTree.step._
import org.errorHandling.ErrorStrings
import org.admin.Admin
import org.configSettings.ConfigSettings
import org.client.Client

class TestScenario4 {
    Admin.setConnectPathForConfigClient("C0000001", "http://configuration/config_1")
    
    val client: org.client.ConfigClient = Client.setClient("http://configuration/config_1")
    
  def scenario4 = {
    
    
    
    println("############################## SCENARIO 4 #######################")
    
    startConfig
    
    error1
    
    error2
    
    error3
    
//    error4
    
    error5
    
    error6
    
    error7
    
    error8
  }
  
  def startConfig = {
    
//        ConfigMgr.currentConfig.clear()
        val step = ConfigMgr.startConfig(client)
        
        require(step.isInstanceOf[FirstStep] == true)
        require(step.id == "S000001", "id must be 001")
        require(step.selectionCriterium.max == "1")
        require(step.selectionCriterium.min == "1")
        require(step.nextStep(0).step == "S000002")
        require(step.nextStep(0).byComponent == "S000001C000001")
        require(step.nextStep(1).step == "S000002")
        require(step.nextStep(1).byComponent == "S000001C000002")
        require(step.nextStep(2).step == "S000003")
        require(step.nextStep(2).byComponent == "S000001C000003")
        
        require(step.components(0).id == "S000001C000001")
        require(step.components(0).nameToShow == "Component 1")
        require(step.components(0).isInstanceOf[ImmutableComponent] == true)
        
        require(step.components(1).id == "S000001C000002")
        require(step.components(1).nameToShow == "Component 2")
        require(step.components(1).isInstanceOf[ImmutableComponent] == true)
        
        require(step.components(2).id == "S000001C000003")
        require(step.components(2).nameToShow == "Component 3")
        require(step.components(2).isInstanceOf[ImmutableComponent] == true)
        
//        require(ConfigMgr.currentConfig.size == 0, "CurrentConfig size is 0")
    }
  
  
  def error1 = {
      
      val step = ConfigMgr.getNextStep(client, Set(  new SelectedComponent("S000003C000003", -1),
                                             new SelectedComponent("S000003C000004", 12)))
      
      require(step.isInstanceOf[ErrorStep] == true, step.errorMessage)
      
      require(step.id == "7")
      require(step.errorMessage == "show error string in ErrorComponent", step.errorMessage)
      require(step.errorComponent.size == 2, step.errorComponent.size) 
      require(step.errorComponent(0).id == "7", step.errorComponent(0).id)
      require(step.errorComponent(0).errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
      require(step.errorComponent(1).id == "7", step.errorComponent(0).id)
      require(step.errorComponent(1).errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
  }
  
    def error2 = {
      
      val step = ConfigMgr.getNextStep(client, Set(  new SelectedComponent("S000003C000003", 2),
                                             new SelectedComponent("S000003C000004", 12)))
      
      require(step.isInstanceOf[ErrorStep] == true, step.errorMessage)
      
      require(step.id == "7")
      require(step.errorMessage == "show error string in ErrorComponent", step.errorMessage)
      require(step.errorComponent.size == 1, step.errorComponent.size)
      require(step.errorComponent(0).id == "7", step.errorComponent(0).id)
      require(step.errorComponent(0).errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
  }
    
  def error3 = {
    
    val step = ConfigMgr.getNextStep(client, Set(  new SelectedComponent("S000003C000003", -1),
                                           new SelectedComponent("S000003C000004", 9)))
    
    require(step.isInstanceOf[ErrorStep] == true, step.errorMessage)
    
    require(step.id == "7")
    require(step.errorMessage == "show error string in ErrorComponent", step.errorMessage)
    require(step.errorComponent.size == 1, step.errorComponent.size) 
    require(step.errorComponent.head.id == "7", step.errorComponent(0).id)
    require(step.errorComponent.head.errorMessage == "minValue is smaller or maxValue is greater as definition in configSttings")
  }
  
//  def error4 = {
//    
//    val step = ConfigMgr.getNextStep(Set(  new SelectedComponent("003001", 2),
//                                           new SelectedComponent("003002", 8)))
//    
//    require(step.isInstanceOf[ErrorStep] == true, step.toString())
//    
//    require(step.id == "7")
//    require(step.errorMessage == ErrorStrings.fromErrorComponent, step.errorMessage)
//    require(step.errorComponent.size == 2, step.errorComponent.size)
//    require(step.errorComponent.head.id == "7", step.errorComponent.head.id)
//    require(step.errorComponent.head.errorMessage == ErrorStrings.valueForImmutableComponent, step.errorComponent.head.errorMessage)
//    require(step.errorComponent.tail.head.id == "7", step.errorComponent.head.id)
//    require(step.errorComponent.tail.head.errorMessage == ErrorStrings.valueForImmutableComponent, step.errorComponent.head.errorMessage)
//  }
  
  
  def error5 = {
    
    Admin.setConnectPathForConfigClient("C0000001", "http://configuration/config_2")
    
    val client: org.client.ConfigClient = Client.setClient("http://configuration/config_2")
    
    val step = ConfigMgr.startConfig(client)
    
    require(step.isInstanceOf[ErrorStep] == true, step.toString())
    
    require(step.id == "7")
    require(step.errorMessage == ErrorStrings.existigOfMoreFirstStep, step.errorMessage)
    require(step.errorComponent.size == 0, step.errorComponent.size)
  }
  
  def error6 = {
    
    val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000005C000001", 0)))
    
    require(step.isInstanceOf[ErrorStep] == true, step.toString())
    
    require(step.id == "7")
    require(step.errorMessage == ErrorStrings.selectionFewComponents, step.errorMessage)
    require(step.errorComponent.size == 0, step.errorComponent.size)
  }
  
  def error7 = {
    
    val step = ConfigMgr.getNextStep(client, Set(  new SelectedComponent("S000005C000001"),
                                           new SelectedComponent("S000005C000002"),
                                           new SelectedComponent("S000005C000003"),
                                           new SelectedComponent("S000005C000004"),
                                           new SelectedComponent("S000005C000005")))
    
    require(step.isInstanceOf[ErrorStep] == true, step.toString())
    
    require(step.id == "7")
    require(step.errorMessage == ErrorStrings.selectionMatchComponents, step.errorMessage)
    require(step.errorComponent.size == 0, step.errorComponent.size)
  }
  
  def error8 = {
    val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000010C000002")))
                                                 
    require(step.isInstanceOf[ErrorStep] == true, step.toString())
    
    require(step.id == "7")
    require(step.errorMessage == ErrorStrings.notFoundSteps, step.errorMessage)
    require(step.errorComponent.size == 0, step.errorComponent.size)
  }
  
  def error9 = {
    
    val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000004C000002"), 
                                                 new SelectedComponent("S000004C000003")))
    
    require(step.isInstanceOf[ErrorStep] == true, step.toString())
    
    require(step.id == "7")
    require(step.errorMessage == ErrorStrings.notFoundNextStep, step.errorMessage)
    require(step.errorComponent.size == 0, step.errorComponent.size)
  }
  
  
}