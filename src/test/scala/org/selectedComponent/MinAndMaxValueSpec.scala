package org.selectedComponent

import org.specs2.Specification
import org.configTree.step.ErrorStep
import org.configMgr._
import org.configTree.component._
import org.configTree.step._

class MinAndMaxValueSpec extends Specification{
  def is = s2"""
    Diese Specification prüft , dass ausgewaelte value im Bereich min und max ist
    
        im Berech -> value = 5                                              $e1
        kleiner als minValue = 0 -> value = -1                              $e2
        groesser als maxValue = 10 -> value = 13                            $e3
        value in ImmutableComponent                                         $e4
    """
  
  val errorStep = new ErrorStep("7",Nil, List("minValue is smaller or naxValue is greater as definition in configSttings"))
  
  val defaulStep = new DefaultStep("004","step 004",
      List(  new NextStep("1","004001","005"), 
             new NextStep("1","004002","005"),
             new NextStep("1","004003","006")),
             SelectionCriterium("1","2"),
             Source("xml","",""),
             List(
                 new ImmutableComponent("004001","component 004001"), 
                 new ImmutableComponent("004002","component 004002"), 
                 new ImmutableComponent("004003","component 004003")))
  
  def e1 = ConfigMgr.getNextStep(Set(new SelectedComponent("003003",5), new SelectedComponent("003001"))) must_== defaulStep
  def e2 = ConfigMgr.getNextStep(Set(new SelectedComponent("003003",-1), new SelectedComponent("003001"))) must_== errorStep
  def e3 = ConfigMgr.getNextStep(Set(new SelectedComponent("003003",13), new SelectedComponent("003001"))) must_== errorStep
  def e4 = ConfigMgr.getNextStep(Set(new SelectedComponent("003001",5), new SelectedComponent("003002"))) must_== errorStep
}