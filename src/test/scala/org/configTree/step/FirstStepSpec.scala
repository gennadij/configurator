package org.configTree.step

import org.specs2.Specification
import org.configMgr.ConfigMgr
import org.configTree.component._

class FirstStepSpec extends Specification{ 
  sequential 
  
  def is = s2"""
  
  This Specification check first step
      
      First step has type FirstStep         $e1
  """
  val configMgr = new ConfigMgr
  
  val firstStep = new FirstStep("001","step 001", List(
      new NextStep("1", "001001","002"), 
      new NextStep("1", "001002","002"), 
      new NextStep("1", "001003","003")),
      new SelectionCriterium("1","1"), new Source("xml","",""),
      List(
          new ImmutableComponent("001001","component 001001"), 
          new ImmutableComponent("001002","component 001002"), 
          new ImmutableComponent("001003","component 001003")))
  
  def e1 = configMgr.startConfig must_== firstStep
  
}