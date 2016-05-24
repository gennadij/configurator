package org

import org.configMgr._
import org.configTree.step._
import org.configTree.component._
import scala.collection.mutable.ListBuffer
import org.specs2.Specification



class RunConfigNotLinear extends Specification{ 
  def is =
  
  s2"""
      Specification for whole Config by step 001, 003, 004, 006
        Step 001                        $e1
        Step 003                        $e2
        step 004                        $e3
        step 006                        $e4
        step 003                        $e5
    """

  val configMgr = new ConfigMgr
  
  configMgr.container.currentConfig.clear()
  
  def e1 = configMgr.startConfig.id must_== "001"
  
  val selectedComponent001003 = "001003"
  
  def e2 = configMgr.getNextStep(List(selectedComponent001003)).id must_== "003"
  
  val selectedComponent003002 = "003002"
  
  def e3 = configMgr.getNextStep(List("003002")).id must_== "004"
  
  val selectedComponent004003 = "004003"
  
  def e4 = configMgr.getNextStep(List("004003")).id must_== "006"
  
  val selectedComponent006001 = "006001"
  
  val selctionComponent003003 = "003003"
  
  def e5 = configMgr.getNextStep(List(selctionComponent003003)).id must_== "004"
                                  
//  val currentConfig = ListBuffer(
//                  new DefaultStep("001","step 001",List(new NextStep("001001","002"), new NextStep("001002","002"), 
//                  new NextStep("001003","003")),"first",new SelectionCriterium("1","1"),new Source("xml","",""), 
//                  List(
//                      new ImmutableComponent("001003","immutable","component 001003")
//                      ))
//                  ,
//                 new DefaultStep("003","step 003",List(new NextStep("003001","004"), 
//                 new NextStep("003002","004"), new NextStep("003003","004"), 
//                 new NextStep("003004","004")),"default",new SelectionCriterium("1","1"),
//                 new Source("xml","",""), 
//                   List(
//                      new ImmutableComponent("003003","immutable","component 003003")
//                      ))) 
//             new DefaultStep("004","step 004",List(new NextStep("004001","005"),
//                 new NextStep("004002","005"), new NextStep("004003","006")),
//                 "default",new SelectionCriterium("1","1"),new Source("xml","",""), 
//                 List(
//                      new StaticComponent("004003","immutable","component 004003")
//                      )), 
//             new DefaultStep("006","step 006", List(new NextStep("006001","000"), new NextStep("006002","000"), 
//                 new NextStep("006003","000")),"last",new SelectionCriterium("1","1"),new Source("xml","",""),
//                 List(
//                      new StaticComponent("006001","immutable","component 006001")
//                      ))
//              )
                      
                                            
  
//  def e6 = configMgr.container.currentConfig must_== currentConfig
  
}