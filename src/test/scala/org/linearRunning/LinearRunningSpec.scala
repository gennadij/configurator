package org.linearRunning

import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings
import org.specs2.mutable._
import org.specs2.specification.Scope
/**
  * Created by gennadi on 05.05.16.
  */
class LinearRunningSpec extends Specification {
  "1. Step 001 contain 3 Components" in new CurrentConfig {
    val firstStep = configMgr.startConfig(container)
    firstStep.components.length must_== 3
  }

  "2. Selection Component 001001 -> nextStepID=002" in new CurrentConfig {
    val selection1 = "001001"
    val nextStep1 = configMgr.getNextStep(container, selection1)
    nextStep1._1.id must beEqualTo("002")
  }

  "3. CurrentConfig size = 1" in new CurrentConfig {
    configMgr.addStepToCurrentConfig(container, "001001")
    container.currentConfig.size must_==1
  }

  "4. CurrentConfig step(0) id=001|components size 1|component id=001001 " in new CurrentConfig {
    container.currentConfig(0).id  must beEqualTo("001")
    container.currentConfig(0).components.size must_==1
    container.currentConfig(0).components(0).id must beEqualTo("001001")
  }

  //=========================================================================================

  "5. Selection Component 002001 -> nextStepID=003" in new CurrentConfig {
    val selection2 = "002001"
    configMgr.addStepToCurrentConfig(container, selection2)
    val nextStep2 = configMgr.getNextStep(container, selection2)
    nextStep2._1.id must beEqualTo("003")
  }

//  "6. CurrentConfig size = 2" in new CurrentConfig {
//    container.currentConfig.size must_==2
//  }

  "7. CurrentConfig step(1) id=002|components size 1|component id=002001 " in new CurrentConfig {
    container.currentConfig(1).id  must beEqualTo("002")
    container.currentConfig(1).components.size must_==1
    container.currentConfig(1).components(0).id must beEqualTo("002001")
  }

  //======================================================================================

  "8. Selection Component 003001 -> nextStepID=003" in new CurrentConfig {
    val selection3 = "003001"
    configMgr.addStepToCurrentConfig(container, selection3)
    val nextStep2 = configMgr.getNextStep(container, selection3)
    nextStep2._1.id must beEqualTo("004")
  }

//  "9. CurrentConfig size = 3" in new CurrentConfig {
//    container.currentConfig.size must_==3
//  }

  "10. CurrentConfig step(2) id=003" in new CurrentConfig {
    container.currentConfig(2).id  must beEqualTo("003")
  }

  "11. CurrentConfig components size 1" in new CurrentConfig {
    container.currentConfig(2).components.size must_==1
  }

  "12. CurrentConfig component id=003001 " in new CurrentConfig {
    container.currentConfig(2).components(0).id must beEqualTo("003001")
  }

  //=======================================================================

  "13. Selection Component 004001 -> nextStepID=005" in new CurrentConfig {
    val selection4 = "004001"
    configMgr.addStepToCurrentConfig(container, selection4)
    val nextStep2 = configMgr.getNextStep(container, selection4)
    nextStep2._1.id must beEqualTo("005")
  }

//  "14. CurrentConfig size = 4" in new CurrentConfig {
//    container.currentConfig.size must_==4
//  }

  "15. CurrentConfig step(3) id=004" in new CurrentConfig {
    container.currentConfig(3).id  must beEqualTo("004")
  }

  "16. CurrentConfig components size 1" in new CurrentConfig {
    container.currentConfig(3).components.size must_==1
  }

  "17. CurrentConfig component id=004001 " in new CurrentConfig {
    container.currentConfig(3).components(0).id must beEqualTo("004001")
  }

  //============================================================================

  "18. Selection Component 005001 -> nextStepID=006" in new CurrentConfig {
    val selection5 = "005001"
    configMgr.addStepToCurrentConfig(container, selection5)
    val nextStep2 = configMgr.getNextStep(container, selection5)
    nextStep2._1.id must beEqualTo("007")
  }

//  "19. CurrentConfig size = 5" in new CurrentConfig {
//    container.currentConfig.size must_==5
//  }

  "20. CurrentConfig step(4) id=004" in new CurrentConfig {
    container.currentConfig(4).id  must beEqualTo("005")
  }

  "21. CurrentConfig components size 1" in new CurrentConfig {
    container.currentConfig(4).components.size must_==1
  }

  "22. CurrentConfig component id=005001 " in new CurrentConfig {
    container.currentConfig(4).components(0).id must beEqualTo("005001")
  }

  //=============================================================================

  "23. Selection Component 007001 -> nextStep=null" in new CurrentConfig {
    val selection7 = "007001"
    configMgr.addStepToCurrentConfig(container, selection7)
    val nextStep2 = configMgr.getNextStep(container, selection7)
    nextStep2._1 must beEqualTo(null)
  }

  "24. CurrentConfig step(5) id=007" in new CurrentConfig {
    container.currentConfig(5).id  must beEqualTo("007")
  }

  "25. CurrentConfig components size 1" in new CurrentConfig {
    container.currentConfig(5).components.size must_==1
  }

  "26. CurrentConfig component id=007001 " in new CurrentConfig {
    container.currentConfig(5).components(0).id must beEqualTo("007001")
  }

}

trait CurrentConfig extends Scope  {
  val configMgr = new ConfigMgr
  val container = ConfigSettings.configSettings
}
