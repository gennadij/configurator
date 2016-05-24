package org.selectionCriterium

import org.specs2.Specification
import org.configTree.step._
import org.configTree.component._
import org.configMgr.ConfigMgr

class SelectionCriteriumSpec extends Specification{
  def is = s2"""
    
    This Spec test SelectiumCriterium
      1. Methode checkSelectionCriterium return step 003 for selection
      002001, 002002 by SelectionCriterium min 1 and max 2                   $e1
      2. Methode checkSelectionCriterium return ErrorStep for selection
      002001, 002001, by SelectionCriterium min 1 and max 2                  $e2
      3. Methode checkSelectionCriterium return ErrorStep for selection
      001001, 001002, by SelectionCriterium min 1 and max 1                  $e3
      4. Methode checkSelectionCriterium return step 002 for selection
      001001, by SelectionCriterium min 1 and max 1                          $e4
      5. Methode checkSelectionCriterium return step 004 for selection
      003001, 003002, 003003 by SelectionCriterium min 2 and max 4           $e5
      6. Methode checkSelectionCriterium return step 004 for selection
      003001, 003002, by SelectionCriterium min 2 and max 4                  $e6
      7. Methode checkSelectionCriterium return ErrorStep for selection
      003001 by SelectionCriterium min 2 and max 4                           $e7
      
      
      
      
    """
      
  val step002 = new DefaultStep("002","step 002", List(
      new NextStep("1", "next step", "002001","003"), 
      new NextStep("1", "next step", "002002","003")),
      new SelectionCriterium("1","2"), new Source("xml","",""),
      List(
          new ImmutableComponent("002001","component 002001"), 
          new ImmutableComponent("002002","component 002002")))
  
  val step003 = new DefaultStep("003","step 003",List(
      new NextStep("1", "next step","003001","004"), 
      new NextStep("1", "next step","003002","004"), 
      new NextStep("1", "next step","003003","004"), 
      new NextStep("1", "next step","003004","004")),
      new SelectionCriterium("2","4"),
      new Source("xml","",""), 
                   List(
                      new ImmutableComponent("003001","component 003001"),
                      new ImmutableComponent("003002","component 003002"),
                      new MutableComponent("003003","component 003003"),
                      new MutableComponent("003004","component 003004")
                      ))
  
  val step004 = new DefaultStep("004","step 004",List(
      new NextStep("1", "next step","004001","005"),
      new NextStep("1", "next step","004002","005"),
      new NextStep("1", "next step","004003","006")),
      new SelectionCriterium("1","1"),new Source("xml","",""), 
                 List(
                      new ImmutableComponent("004001","component 004001"),
                      new ImmutableComponent ("004002","component 004002"),
                      new ImmutableComponent("004003","component 004003")
                      ))
  
  val errorStepFore2 = new ErrorStep("7", "error step", "it was selected same components")
  
  val errorStepFore3 = new ErrorStep("7", "error step", "ivalible selectiumCriterium or "+
            "was selected to mach or to few Components")
  
  
  def e1 = ConfigMgr.getNextStep(List("002001", "002002")) must_== step003

  def e2 = ConfigMgr.getNextStep(List("002001", "002001")) must_== errorStepFore2
  
  def e3 = ConfigMgr.getNextStep(List("001001", "001002")) must_== errorStepFore3

  def e4 = ConfigMgr.getNextStep(List("001001")) must_== step002
  
  def e5 = ConfigMgr.getNextStep(List("003001", "003002", "003003")) must_== step004
  
  def e6 = ConfigMgr.getNextStep(List("003001", "003002")) must_== step004
  
  def e7 = ConfigMgr.getNextStep(List("003001")) must_== errorStepFore3
}