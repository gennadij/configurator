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
    
    /*
     * Regestrierung AdminUser
     */
//    val status = Admin.register(adminId, adminUsername, adminPassword)
//    println(status.message)
    
    /*
     * Anmelden von AdminUser
     */
    
    val status = Admin.connect(adminUsername, adminPassword)
    println(status.message)
    
    println(step1(adminId).message)
    
    
  }
  
  def step1(adminId: String) = {
        
    /*
     * create 1. Step
     */
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
    
    
    Admin.setStep(adminId, true, step)
  }
  
  def step2(adminId: String) = {
        
    /*
     * create 2. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000002C000001", ""),
        new ImmutableComponent("S000002C000002", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000002C000001", "S000003"),
      new NextStep("", "S000002C000002", "S000003")
    )
    
    val step = new DefaultStep("S000002", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, false, step)
  }
  
  def step3(adminId: String) = {
        
    /*
     * create 3. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000003C000001", ""),
        new ImmutableComponent("S000003C000002", ""),
        new ImmutableComponent("S000003C000003", ""),
        new ImmutableComponent("S000003C000004", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000003C000001", "S000004"),
      new NextStep("", "S000003C000002", "S000004"),
      new NextStep("", "S000003C000003", "S000004"),
      new NextStep("", "S000003C000003", "S000004")
    )
    
    val step = new DefaultStep("S000003", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, true, step)
  }
  
  def step4(adminId: String) = {
        
    /*
     * create 4. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000004C000001", ""),
        new ImmutableComponent("S000004C000002", ""),
        new ImmutableComponent("S000004C000003", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000004C000001", "S000005"),
      new NextStep("", "S000004C000002", "S000006"),
      new NextStep("", "S000004C000003", "S000006")
    )
    
    val step = new DefaultStep("S000004", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, true, step)
  }
  
  def step5(adminId: String) = {
        
    /*
     * create 5. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000005C000001", ""),
        new ImmutableComponent("S000005C000002", ""),
        new ImmutableComponent("S000005C000003", ""),
        new ImmutableComponent("S000005C000004", ""),
        new ImmutableComponent("S000005C000005", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000005C000001", "S000007"),
      new NextStep("", "S000005C000002", "S000007"),
      new NextStep("", "S000005C000003", "S000007"),
      new NextStep("", "S000005C000003", "S000008"),
      new NextStep("", "S000005C000003", "S000008")
    )
    
    val step = new DefaultStep("S000005", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, true, step)
  }
  
  def step6(adminId: String) = {
        
    /*
     * create 6. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000006C000001", ""),
        new ImmutableComponent("S000006C000002", ""),
        new ImmutableComponent("S000006C000003", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000006C000001", "S000000"),
      new NextStep("", "S000006C000002", "S000000"),
      new NextStep("", "S000006C000003", "S000000")
    )
    
    val step = new DefaultStep("S000006", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, true, step)
  }
  
  def step7(adminId: String) = {
        
    /*
     * create 7. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000007C000001", ""),
        new ImmutableComponent("S000007C000002", ""),
        new ImmutableComponent("S000007C000003", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000007C000001", "S000000"),
      new NextStep("", "S000007C000002", "S000000"),
      new NextStep("", "S000007C000003", "S000000")
    )
    
    val step = new DefaultStep("S000007", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, true, step)
  }
  
  def step8(adminId: String) = {
        
    /*
     * create 6. Step
     */
    val components = List[Component](
        new ImmutableComponent("S000008C000001", ""),
        new ImmutableComponent("S000008C000002", "")
    )
    
    val nextStep = List[NextStep] (
      new NextStep("", "S000008C000001", "S000000"),
      new NextStep("", "S000008C000002", "S000000")
    )
    
    val step = new DefaultStep("S000008", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
    Admin.setStep(adminId, true, step)
  }
}