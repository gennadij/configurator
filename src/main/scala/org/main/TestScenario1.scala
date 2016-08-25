package org.main

import org.configMgr._
import org.configTree.component._
import org.configTree.step._
import org.admin.Admin
import org.client.Client

class TestScenario1 {
  def scenario1 = {

    Admin.setConnectPathForConfigClient("C0000001", "http://configuration/config_1")
    
    val client: org.client.ConfigClient = Client.setClient("http://configuration/config_1")
    
    println("############################## SCENARIO 1 #######################")
    
    startConfig
    
    step1
    
    step2
    
    step3
    
    step4
    
    step5
    
    step6
  
    def startConfig = {
        
        val step = ConfigMgr.startConfig(client)
         
        require(step.isInstanceOf[FirstStep] == true, step)
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
        
        require(client.currentConfig.size == 0, "CurrentConfig size is 0")
    }
    
    def step1 = {
      
      val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000001C000001")))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      require(step.id == "S000002", step.id)
      require(step.selectionCriterium.min == "1")
      require(step.selectionCriterium.max == "2")
      require(step.nextStep(0).step == "S000003")
      require(step.nextStep(0).byComponent == "S000002C000001")
      require(step.nextStep(1).step == "S000003")
      require(step.nextStep(1).byComponent == "S000002C000002")
      
      require(step.components(0).id == "S000002C000001")
      require(step.components(0).nameToShow == "Component 1")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "S000002C000002")
      require(step.components(1).nameToShow == "Component 2")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      
      require(client.currentConfig.size == 1, client.currentConfig.size)
      require(client.currentConfig.last.size == 1, "CurrentConfig size is 1")
      require(client.currentConfig.last(0).id == "S000001")
      require(client.currentConfig.last(0).nameToShow == "Step 1")
      require(client.currentConfig.last(0).components.size == 1)
      require(client.currentConfig.last(0).components(0).id == "S000001C000001")
      require(client.currentConfig.last(0).components(0).nameToShow == "Component 1")
    }
    
    def step2 = {
      
      val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000002C000001")))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      
      require(step.id == "S000003", step.id)
      
      require(step.selectionCriterium.min == "2")
      require(step.selectionCriterium.max == "4")
  
      require(step.nextStep(0).byComponent == "S000003C000001")
      require(step.nextStep(0).step == "S000004")
      require(step.nextStep(1).byComponent == "S000003C000002")
      require(step.nextStep(1).step == "S000004")
      require(step.nextStep(2).byComponent == "S000003C000003")
      require(step.nextStep(2).step == "S000004")
      require(step.nextStep(3).byComponent == "S000003C000004")
      require(step.nextStep(3).step == "S000004")
      
      require(step.components(0).id == "S000003C000001")
      require(step.components(0).nameToShow == "Component 1")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "S000003C000002")
      require(step.components(1).nameToShow == "Component 2")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "S000003C000003")
      require(step.components(2).nameToShow == "Component 3")
      require(step.components(2).isInstanceOf[MutableComponent] == true)
      require(step.components(2).minValue == 0)
      require(step.components(2).maxValue == 10)
      require(step.components(2).defaultValue == 5)
      require(step.components(2).interval == 1)
      require(step.components(2).intervals == List.empty)
      
      require(step.components(3).id == "S000003C000004")
      require(step.components(3).nameToShow == "Component 4")
      require(step.components(3).isInstanceOf[MutableComponent] == true)
      require(step.components(3).minValue == 0)
      require(step.components(3).maxValue == 10)
      require(step.components(3).defaultValue == 5)
      require(step.components(3).interval == 1)
      require(step.components(3).intervals == List.empty)
      
      require(client.currentConfig.size == 2, client.currentConfig.size)
      require(client.currentConfig.last.size == 2, client.currentConfig.last.size)
      require(client.currentConfig.last(1).id == "S000002", client.currentConfig.last(1).id)
      require(client.currentConfig.last(1).nameToShow == "Step 2")
      require(client.currentConfig.last(1).components.size == 1)
      require(client.currentConfig.last(1).components(0).id == "S000002C000001")
      require(client.currentConfig.last(1).components(0).nameToShow == "Component 1")
    }
    
    def step3 = {
      
      val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000003C000001"), 
                                           new SelectedComponent("S000003C000002")))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      
      require(step.id == "S000004", step.id)
      
      require(step.selectionCriterium.min == "1")
      require(step.selectionCriterium.max == "2")
  
      require(step.nextStep(0).byComponent == "S000004C000001")
      require(step.nextStep(0).step == "S000005")
      require(step.nextStep(1).byComponent == "S000004C000002")
      require(step.nextStep(1).step == "S000005")
      require(step.nextStep(2).byComponent == "S000004C000003")
      require(step.nextStep(2).step == "S000006")
      
      require(step.components(0).id == "S000004C000001")
      require(step.components(0).nameToShow == "Component 1")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "S000004C000002")
      require(step.components(1).nameToShow == "Component 2")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "S000004C000003")
      require(step.components(2).nameToShow == "Component 3")
      require(step.components(2).isInstanceOf[ImmutableComponent] == true)
  
      require(client.currentConfig.size == 3, "CurrentConfig size is 3")
      require(client.currentConfig.last.size == 3, "CurrentConfig size is 3")
      require(client.currentConfig.last(2).id == "S000003")
      require(client.currentConfig.last(2).nameToShow == "Step 3")
      require(client.currentConfig.last(2).components.size == 2)
      require(client.currentConfig.last(2).components(0).id == "S000003C000001")
      require(client.currentConfig.last(2).components(0).nameToShow == "Component 1")
      require(client.currentConfig.last(2).components(1).id == "S000003C000002")
      require(client.currentConfig.last(2).components(1).nameToShow == "Component 2")
    }
    
    def step4 = {
      
      val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000004C000001")))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[DefaultStep] == true)
      
      require(step.id == "S000005")
      
      require(step.selectionCriterium.min == "2")
      require(step.selectionCriterium.max == "4")
  
      require(step.nextStep(0).byComponent == "S000005C000001")
      require(step.nextStep(0).step == "S000007")
      require(step.nextStep(1).byComponent == "S000005C000002")
      require(step.nextStep(1).step == "S000007")
      require(step.nextStep(2).byComponent == "S000005C000003")
      require(step.nextStep(2).step == "S000007")
      require(step.nextStep(3).byComponent == "S000005C000004")
      require(step.nextStep(3).step == "S000008")
      require(step.nextStep(4).byComponent == "S000005C000005")
      require(step.nextStep(4).step == "S000008")
      
      require(step.components(0).id == "S000005C000001")
      require(step.components(0).nameToShow == "Component 1")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "S000005C000002")
      require(step.components(1).nameToShow == "Component 2")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "S000005C000003")
      require(step.components(2).nameToShow == "Component 3")
      require(step.components(2).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(3).id == "S000005C000004")
      require(step.components(3).nameToShow == "Component 4")
      require(step.components(3).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(4).id == "S000005C000005")
      require(step.components(4).nameToShow == "Component 5")
      require(step.components(4).isInstanceOf[ImmutableComponent] == true)
  
      require(client.currentConfig.size == 4, client.currentConfig.size)
      require(client.currentConfig.last.size == 4, "CurrentConfig size is 4")
      require(client.currentConfig.last(3).id == "S000004")
      require(client.currentConfig.last(3).nameToShow == "Step 4")
      require(client.currentConfig.last(3).components.size == 1)
      require(client.currentConfig.last(3).components(0).id == "S000004C000001")
      require(client.currentConfig.last(3).components(0).nameToShow == "Component 1")
    }
    
    def step5 = {
      
      val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000005C000001"), 
                                              new SelectedComponent("S000005C000002")))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[LastStep] == true)
      
      require(step.id == "S000007")
      
      require(step.selectionCriterium.min == "1")
      require(step.selectionCriterium.max == "1")
  
      require(step.components(0).id == "S000007C000001")
      require(step.components(0).nameToShow == "Component 1")
      require(step.components(0).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(1).id == "S000007C000002")
      require(step.components(1).nameToShow == "Component 2")
      require(step.components(1).isInstanceOf[ImmutableComponent] == true)
      
      require(step.components(2).id == "S000007C000003", step.components(2).id)
      require(step.components(2).nameToShow == "Component 3")
      require(step.components(2).isInstanceOf[ImmutableComponent] == true)
      
      require(client.currentConfig.size == 5, client.currentConfig.size)
      require(client.currentConfig.last.size == 5, "CurrentConfig size is 5")
      require(client.currentConfig.last(4).id == "S000005")
      require(client.currentConfig.last(4).nameToShow == "Step 5")
      require(client.currentConfig.last(4).components.size == 2)
      require(client.currentConfig.last(4).components(0).id == "S000005C000001")
      require(client.currentConfig.last(4).components(0).nameToShow == "Component 1")
      require(client.currentConfig.last(4).components(1).id == "S000005C000002")
      require(client.currentConfig.last(4).components(1).nameToShow == "Component 2")
    }
    
    def step6 = {
      
      val step = ConfigMgr.getNextStep(client, Set(new SelectedComponent("S000007C000001")))
      
      require(step.isInstanceOf[ErrorStep] != true, step.errorMessage)
  
      require(step.isInstanceOf[FinalStep] == true)
      
      require(step.id == "FS000000")
      
      require(client.currentConfig.size == 6, "CurrentConfig size is 6")
      require(client.currentConfig.last.size == 6, "CurrentConfig size is 6")
      require(client.currentConfig.last(5).id == "S000007")
      require(client.currentConfig.last(5).nameToShow == "Step 7")
      require(client.currentConfig.last(5).components.size == 1)
      require(client.currentConfig.last(5).components(0).id == "S000007C000001")
      require(client.currentConfig.last(5).components(0).nameToShow == "Component 1")
    }
  }
}