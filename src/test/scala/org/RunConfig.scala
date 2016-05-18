package org

import org.specs2.Specification
import org.configMgr._
import org.configTree.step._
import org.configTree.component._
import scala.collection.mutable.ListBuffer

/**
  * Created by gennadi on 12.05.16.
  */
class RunConfig extends Specification{ def is =
  
  s2"""
      Specification for whole Config by step 001, 003, 004, 006
        Step 001                        $e1
        Step 003                        $e2
        step 004                        $e3
        step 006                        $e4
    """

  val configMgr = new ConfigMgr
  
  def e1 = configMgr.startConfig.id must_== "001"
  
  val selectedComponent001003 = "001003"
  
  def e2 = configMgr.getNextStep(selectedComponent001003).id must_== "003"
  
  val selectedComponent003002 = "003002"
  
  def e3 = configMgr.getNextStep("003002").id must_== "004"
  
  val selectedComponent004003 = "004003"
  
  def e4 = configMgr.getNextStep("004003").id must_== "006"
  
  val selectedComponent006001 = "006001"
  
                                  
//  val currentConfig = ListBuffer(new DefaultStep("001","step 001",List(new NextStep("001001","002"), new NextStep("001002","002"), 
//                  new NextStep("001003","003")),"first",new SelectionCriterium("1","1"),new Source("xml","",""), 
//                  List(
//                      new ImmutableComponent("001003","immutable","component 001003")
//                      )),
//             new DefaultStep("003","step 003",List(new NextStep("003001","004"), 
//                 new NextStep("003002","004"), new NextStep("003003","004"), 
//                 new NextStep("003004","004")),"default",new SelectionCriterium("1","1"),
//                 new Source("xml","",""), 
//                 List(
//                      new ImmutableComponent("003002","immutable","component 003002")
//                      )), 
//             new DefaultStep("004","step 004",List(new NextStep("004001","005"),
//                 new NextStep("004002","005"), new NextStep("004003","006")),
//                 "default",new SelectionCriterium("1","1"),new Source("xml","",""), 
//                 List(
//                      new ImmutableComponent("004003","immutable","component 004003")
//                      )), 
//             new DefaultStep("006","step 006", List(new NextStep("006001","000"), new NextStep("006002","000"), 
//                 new NextStep("006003","000")),"last",new SelectionCriterium("1","1"),new Source("xml","",""),
//                 List(
//                      new ImmutableComponent("006001","immutable","component 006001")
//                      )))          
//                                            
//  
//  def e5 = {
//    val step1 = configMgr.container.currentConfig filter (_.id == "001")
//    val currentStep1 = currentConfig filter (_.id == "001")
//    val step2 = configMgr.container.currentConfig filter (_.id == "003")
//    val currentStep2 = currentConfig filter (_.id == "003")
//    val step3 = configMgr.container.currentConfig filter (_.id == "004")
//    val currentStep3 = currentConfig filter (_.id == "004")
//    val step4 = configMgr.container.currentConfig filter (_.id == "004")
//    val currentStep4 = currentConfig filter (_.id == "004")
//    (step1 must_== currentStep1) and
//    (step2 must_== currentStep2) and
//    (step3 must_== currentStep3) and
//    (step4  must_== currentStep4) 
//  }
}
