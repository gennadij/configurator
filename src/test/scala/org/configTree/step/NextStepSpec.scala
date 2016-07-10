//package org.configTree.step
//
//import org.configMgr.ConfigMgr
//import org.specs2._
//import org.configTree.component._
//
///**
//  * Created by gennadi on 12.05.16.
//  */
//class NextStepSpec extends Specification {
//  
//  def is = s2"""
//
//    This is specification for next step
//
//    Next Step must
//      1. startConfig                                                        $e1
//      2. component ID=001001 next step=002                                  $e2
//      3. Error -> nextSteps for selectedComponentIds was not same           $e3
//                                                                """
//  
//  def e1 = {
//    val firstStep = new FirstStep(
//    "001",
//    "step 001",
//    List(new NextStep("1", "001001", "002"), new NextStep("1", "001002", "002"),
//          new NextStep("1", "001003", "003")),
//    new SelectionCriterium("1","1"),
//    new Source("xml","",""),
//    List(new ImmutableComponent("001001", "component 001001"),
//          new ImmutableComponent("001002", "component 001002"),
//          new ImmutableComponent("001003", "component 001003")))
//    
//    ConfigMgr.startConfig must_== firstStep
//  }
//  
//  def e2 = {
//    
//    val step002 = new DefaultStep("002", "step 002", List(new NextStep("1", "002001","003"), new NextStep("1", "002002", "003")),
//    new SelectionCriterium("1", "2"), new Source("xml","",""), List(new ImmutableComponent("002001", "component 002001"),
//      new ImmutableComponent("002002", "component 002002")))
//    
//    ConfigMgr.getNextStep(Set(new SelectedComponent("001001"))) must_== step002
//  }
//  
//  val errorE3 = new ErrorStep("7", "nextSteps for selectedComponentIds was not same", Nil)
//  def e3 = ConfigMgr.getNextStep(Set(new SelectedComponent("004002"), new SelectedComponent("004003"))) must_== errorE3
//}