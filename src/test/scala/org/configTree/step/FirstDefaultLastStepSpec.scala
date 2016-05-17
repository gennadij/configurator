package org.configTree.step

import org.specs2.Specification
import org.configMgr.ConfigMgr
import org.configTree.component._

class FirstDefaultLastStepSpec extends Specification{ 
  def is = s2"""
    This Specification check first , default and last step
      First step                         $e1
      Default step                       $e2
      Last step                          $e3
    """
  
  val configMgr = new ConfigMgr
  
  val firstStep = new FirstStep("001","step 001", List(
      new NextStep("001001","002"), 
      new NextStep("001002","002"), 
      new NextStep("001003","003")),
      "first", new SelectionCriterium("1","1"), new Source("xml","",""),
      List(
          new ImmutableComponent("001001","immutable","component 001001"), 
          new ImmutableComponent("001002","immutable","component 001002"), 
          new ImmutableComponent("001003","immutable","component 001003")))
  
  val defaultStep = new DefaultStep("002","step 002", List(
      new NextStep("002001","003"), 
      new NextStep("002002","003")),
      "default", new SelectionCriterium("1","1"), new Source("xml","",""),
      List(
          new ImmutableComponent("002001","immutable","component 002001"), 
          new ImmutableComponent("002002","immutable","component 002002")))
  
  val lastStep = new LastStep("008","step 008", List(
      new NextStep("008001","000"), 
      new NextStep("008002","000")),
      "default", new SelectionCriterium("1","1"), new Source("xml","",""),
      List(
          new ImmutableComponent("008001","immutable","component 008001"), 
          new ImmutableComponent("008002","immutable","component 008002")))
  
  def e1 = configMgr.startConfig.asInstanceOf[FirstStep] must_== firstStep
  def e2 = configMgr.getNextStep("001001").asInstanceOf[DefaultStep] must_== defaultStep
  def e3 = configMgr.getNextStep("005005").asInstanceOf[LastStep] must_== lastStep
  
}