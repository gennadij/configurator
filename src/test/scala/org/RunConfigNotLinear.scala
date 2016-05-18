//package org
//
//import org.configMgr.ConfigMgr
//import org.configSettings.ConfigSettings
//import org.specs2.mutable.Specification
//import org.specs2.specification.Scope
//import org.specs2.specification.BeforeAfterAll
//import org.specs2.specification.BeforeEach
//
//class RunConfigNotLinear extends Specification with BeforeEach{
//  
//  def before = {
//    println("befor")
//  }
////  def beforeAll = {println("beforAll")
////  }
////  def afterAll = {println("afterAll")
////  }
//  
//  // step 1
//  "1. Step 001 contain 3 Components" in new CurrentConfig {
//    val firstStep = configMgr.startConfig
//    firstStep.components.length must_== 3
//  }
//
//  "1. Selection Component 001001 -> nextStepID=002" in new CurrentConfig {
//    val selection = "001001"
//    val nextStep1 = configMgr.getNextStep(selection)
//    nextStep1.id must beEqualTo("002")
//  }
//
////  "1. CurrentConfig step(0) id=001|component id=001001 " in new CurrentConfig {
////    configMgr.addStepToCurrentConfig("001001")
//    
////    val step = configMgr.container.currentConfig filter (_.id == "001")
////    step(0).id  must beEqualTo("001")
////    step(0).components(0).id must beEqualTo("001001")
////  }
//
//  //=========================================================================================
//  // step 2
//  "2. Selection Component 002001 -> nextStepID=003" in new CurrentConfig {
//    val selection2 = "002001"
////    configMgr.addStepToCurrentConfig(selection2)
//    val nextStep2 = configMgr.getNextStep(selection2)
//    nextStep2.id must beEqualTo("003")
//  }
//
//  "2. CurrentConfig step(1) id=002||component id=002001 " in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "002")
//    step(0).id  must beEqualTo("002")
//    step(0).components(0).id must beEqualTo("002001")
//  }
//
//  //======================================================================================
//  // step 3 
//  
//  "3. Selection Component 003001 -> nextStepID=003" in new CurrentConfig {
//    val selection3 = "003001"
////    configMgr.addStepToCurrentConfig(selection3)
//    val nextStep2 = configMgr.getNextStep(selection3)
//    nextStep2.id must beEqualTo("004")
//  }
//
//  "3. CurrentConfig step(2) id=003" in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "003")
//    step(0).id  must beEqualTo("003")
//  }
//
//  "3. CurrentConfig component id=003001 " in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "003")
//    step(0).components(0).id must beEqualTo("003001")
//  }
//
//  //=======================================================================
//  // step 4
//  
//  "4. Selection Component 004001 -> nextStepID=005" in new CurrentConfig {
//    val selection4 = "004001"
////    configMgr.addStepToCurrentConfig(selection4)
//    val nextStep2 = configMgr.getNextStep(selection4)
//    nextStep2.id must beEqualTo("005")
//  }
//
//  "4. CurrentConfig step(3) id=004" in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "004")
//    step(0).id  must beEqualTo("004")
//  }
//
//  "4. CurrentConfig component id=004001 " in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "004")
//    step(0).components(0).id must beEqualTo("004001")
//  }
//
//  //============================================================================
//  //step 5
//  
//  "5. Selection Component 005001 -> nextStepID=006" in new CurrentConfig {
//    val selection5 = "005001"
////    configMgr.addStepToCurrentConfig(selection5)
//    val nextStep2 = configMgr.getNextStep(selection5)
//    nextStep2.id must beEqualTo("007")
//  }
//
//  "5. CurrentConfig step(4) id=005" in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "005")
//    step(0).id  must beEqualTo("005")
//  }
//
//  "5. CurrentConfig component id=005001 " in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "005")
//    step(0).components(0).id must beEqualTo("005001")
//  }
//
//  //=============================================================================
//  //step 6
//  
//  "6. Selection Component 007001 -> nextStep=null" in new CurrentConfig {
//    val selection7 = "007001"
////    configMgr.addStepToCurrentConfig(selection7)
//    val nextStep2 = configMgr.getNextStep(selection7)
//    nextStep2.id must beEqualTo("000")
//    nextStep2.nameToShow must_== "I am final step"
//  }
//
//  "6. CurrentConfig step(5) id=007" in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "006")
//    step(0).id  must beEqualTo("007")
//  }
//
//  "6. CurrentConfig component id=007001 " in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "006")
//    step(0).components(0).id must beEqualTo("007001")
//  }
//  
//  //=============================================================================
//  // step 4
//  
//  "4. Selection Component 004001 -> nextStepID=005" in new CurrentConfig {
//    val selection = "004001"
////    configMgr.addStepToCurrentConfig(selection)
//    val nextStep2 = configMgr.getNextStep(selection)
//    nextStep2.id must beEqualTo("005")
//  }
//
//  "4. CurrentConfig step(3) id=004" in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "004")
//    step(0).id  must beEqualTo("004")
//  }
//
//  "4. CurrentConfig component id=004001 " in new CurrentConfig {
//    val step = configMgr.container.currentConfig filter (_.id == "004")
//    step(0).components(0).id must beEqualTo("004001")
//  }
//  
//  "7. Currentconfig size = ?" in new CurrentConfig {
//    configMgr.container.currentConfig.size must_==4
//  }
//}
//
//trait CurrentConfig extends Scope  {
//  val configMgr = new ConfigMgr
//}