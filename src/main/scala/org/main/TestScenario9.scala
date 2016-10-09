package org.main

import org.admin.Admin
import org.configTree.step.Step
import org.configTree.step.DefaultStep
import org.configTree.component.Component
import org.configTree.component.ImmutableComponent
import org.configTree.step.NextStep

class TestScenario9 {
  
  
  def scenario9 = {
    
    val components = List[Component](
        new ImmutableComponent("S000001C000001", ""),
        new ImmutableComponent("S000001C000002", ""),
        new ImmutableComponent("S000001C000003", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000001C000001", "S000002"),
      new NextStep("", "S000001C000002", "S000002"),
      new NextStep("", "S000001C000003", "S000003")
    )
    
    val step = new DefaultStep("S000001", "nameToShow", "", nextStep, null, null, components, Nil)
    Admin.setStep("config2", true, step)
  }
  
  def scenario9_1() = {
    
    val adminId: String = "AD000001"
    val adminUsername : String = "test1"
    val adminPassword: String = "test"
    
    
//    val status = Admin.register(adminId, adminUsername, adminPassword)
//    println(status.message)
    val status = Admin.connect(adminUsername, adminPassword)
    println(status.message)
    
    val components = List[Component](
        new ImmutableComponent("S000001C000001", ""),
        new ImmutableComponent("S000001C000002", ""),
        new ImmutableComponent("S000001C000003", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000001C000001", "S000002"),
      new NextStep("", "S000001C000002", "S000002"),
      new NextStep("", "S000001C000003", "S000003")
    )
    
    val step = new DefaultStep("S000001", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    val statusStep = Admin.setStep(adminId, true, step)
    
    println(status.message)
  }
  
}