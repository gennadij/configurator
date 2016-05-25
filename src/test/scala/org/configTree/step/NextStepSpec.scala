package org.configTree.step

import org.configMgr.ConfigMgr
import org.specs2._
import org.configTree.component.ImmutableComponent

/**
  * Created by gennadi on 12.05.16.
  */
class NextStepSpec extends Specification {
  
  def is = s2"""

    This is specification for next step

    Next Step must
      1. startConfig                                            $e1
      2. component ID=001001 next step=002                      $e2
                                                                """

  val configMgr = new ConfigMgr
  
  def e1 = {
    val firstStep = new FirstStep(
    "001",
    "step 001",
    List(new NextStep("1", "next step", "001001", "002"), new NextStep("1", "next step", "001002", "002"),
          new NextStep("1", "next step", "001003", "003")),
    new SelectionCriterium("1","1"),
    new Source("xml","",""),
    List(new ImmutableComponent("001001", "component 001001"),
          new ImmutableComponent("001002", "component 001002"),
          new ImmutableComponent("001003", "component 001003")))
    
    configMgr.startConfig must_== firstStep
  }
  
  def e2 = {
    
    val step002 = new DefaultStep("002", "step 002", List(new NextStep("1", "next step", "002001","003"), new NextStep("1", "next step", "002002", "003")),
    new SelectionCriterium("1", "2"), new Source("xml","",""), List(new ImmutableComponent("002001", "component 002001"),
      new ImmutableComponent("002002", "component 002002")))
    
    configMgr.getNextStep(Set("001001")) must_== step002
  }
}