package org

import org.specs2._
import org.configMgr._
import org.configSettings._
import org.container._

class IsolatedSpec extends mutable.Specification { 
  isolated
  "Isolated execution" >> {
    val configMgr = new ConfigMgr
    val container = ConfigSettings.configSettings
    val size = container.currentConfig.size
    container.currentConfig.remove(0, size)
    
    "Current Config must 0 " >> {
      container.currentConfig.size must_==0
    }
    
    "Step 001 contain 3 Components" >> {
      val firstStep = configMgr.startConfig(container)
      firstStep.components.length must_== 3
    }
    
    "Currentconfig size = 0" >> {
      container.currentConfig.size must_==0
    }
    
    "Selection Component 001001 -> nextStepID=002" >> {
      val selection1 = "001001"
      val nextStep1 = configMgr.getNextStep(container, selection1)
      nextStep1._1.id must beEqualTo("002")
    }
    
    "CurrentConfig size = 1" >> {
      configMgr.addStepToCurrentConfig(container, "001001")
      container.currentConfig.size must_==1
    }
    
    "Selection Component 002001 -> nextStepID=003" >> {
      val selection2 = "002001"
      val nextStep2 = configMgr.getNextStep(container, selection2)
      nextStep2._1.id must beEqualTo("003")
    }
    
    "CurrentConfig size = 2" >> {
      configMgr.addStepToCurrentConfig(container, "002001")
      container.currentConfig.size must_==2
    }
    
  }
}