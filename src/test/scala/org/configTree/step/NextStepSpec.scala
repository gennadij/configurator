package org.configTree.step

import org.configMgr.ConfigMgr
import org.configTree.component.StaticComponent
import org.specs2._

/**
  * Created by gennadi on 12.05.16.
  */
class NextStepSpec extends Specification {
  def is = s2"""

    "This is specification for next step

    Next Step must
      1. component ID=001001 next step=002                      $e1
      2. start config                                           $e2
                                                                """

  val configMgr = new ConfigMgr
  val step002 = new DefaultStep("002", "step 002", List(new NextStep("002001","003"), new NextStep("002002", "003")), "default",
    new SelectionCriterium("1", "1"), new Source("","",""), List(new StaticComponent("002001", "immutable", "component 002001"),
      new StaticComponent("002002", "immutable", "component 002002")))

  val firstStep = new FirstStep(
    "001",
    "step 001",
    List(new NextStep("001001", "002"), new NextStep("001002", "002"),
          new NextStep("001003", "003")),
    "first",
    new SelectionCriterium("1","1"),
    new Source("xml","",""),
    List(new StaticComponent("001001", "immutable", "component 001001"),
          new StaticComponent("001002", "immutable", "component 001002"),
          new StaticComponent("001003", "immutable", "component 001003")))



  def e1 = configMgr.getNextStep("001001") must_== step002
  def e2 = configMgr.startConfigV01 must_== firstStep
  def e3 = ???
}