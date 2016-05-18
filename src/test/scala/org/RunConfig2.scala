package org

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeEach
import org.configMgr.ConfigMgr
import org.specs2.specification.AfterEach

class RunConfig2 extends Specification with BeforeEach with AfterEach{
  
  val configMgr = new ConfigMgr
  
  def before = {
    println("before")
    println(configMgr.container.currentConfig.size)
    
  }
  
  def after = {
    println("after")
    println(configMgr.container.currentConfig.size)
  }
  
  "Step 1" >> {
    configMgr.startConfig.id must_== "001"
  }
  
  "Step 2" >> {
    configMgr.getNextStep("001001").id must_== "002" 
  }
  
  "Step 3" >> {
    configMgr.getNextStep("002001").id must_== "003"
  }
  
  "CurrentConfig" >> {
    configMgr.container.currentConfig.size must_== 2
//    configMgr.container.currentConfig(0).components.size must_== 1
  }
  
  
}