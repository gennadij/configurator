package org

import org.specs2.mutable.Specification
import org.specs2.mutable.After
import org.configMgr.ConfigMgr
import org.specs2.mutable.Before

class RunConfig3 extends Specification{
  sequential 
  "Step 1" in new ListMaker {
    configMgr.startConfig.id must_== "001"
//    configMgr.startConfig
  }
  
  "Step 2" in new ListMaker {
    configMgr.getNextStep("001001").id must_== "002" 
//    configMgr.getNextStep("001001")
//      currentConfig.size must_== 1
  }
  
  "Step 3" in new ListMaker {
    configMgr.getNextStep("002001").id must_== "003"
//    configMgr.getNextStep("002001")
//   currentConfig.size must_== 2
  }
  
  "Step 4" in new ListMaker {
    configMgr.getNextStep("003001") 
    currentConfig.size must_== 3
  }
  
  
  trait ListMaker extends After{
    lazy val configMgr = new ConfigMgr
    lazy val currentConfig = configMgr.container.currentConfig
    def after = println("Final " + currentConfig.size)
    def before =  currentConfig.clear()
  }
}