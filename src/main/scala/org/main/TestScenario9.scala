package org.main

import org.admin.Admin
import org.configTree.step.SelectionCriterium
import org.admin.configTree.AdminStep
import org.admin.configTree.AdminComponent
import org.admin.configTree.AdminConfigTree

class TestScenario9 {
  
  def scenario9_1() = {
    
    val adminId: String = "AU#38:1"
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
    
//    val status = Admin.connect(adminUsername, adminPassword)
//    println(status.message)
    
//      step1(adminId)
//    println(step2(adminId).message)
//    println(step3(adminId).message)
//    println(step4(adminId).message)
//    println(step5(adminId).message)
//    println(step6(adminId).message)
//    println(step7(adminId).message)
//    println(step8(adminId).message)
    
    configTree(adminId)
  }
  
  def configTree(adminId: String) = {
    val configTree: AdminConfigTree = Admin.configTree(adminId)
    
    configTree.steps.foreach({
      println
    })
    
    Admin.addStep(null)
    
  }
  
  
  def step1(adminId: String) = {
        
    /*
     * create 1. Step
     */
    
    val step = new AdminStep("", adminId, "first", new SelectionCriterium("1", "1"))
    
    val stepId = Admin.addStep(step)
    
    println(stepId)
    
//    val components = List[AdminComponent](
//          new AdminComponent("", adminId, "immutable"),
//          new AdminComponent("", adminId, "immutable"),
//          new AdminComponent("", adminId, "immutable")
//    )
    
//    val componentIds = components.map (c => {
//      Admin.addComponent(c)
//    })
//    
//    componentIds.foreach(println)
    
    
//    val components = List[Component](
//        new ImmutableComponent("S000001C000001", ""),
//        new ImmutableComponent("S000001C000002", ""),
//        new ImmutableComponent("S000001C000003", "")
//    )
//    val nestSteps = List[NextStep](
//        new NextStep()
//    )
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000001C000001", "S000002"),
//      new NextStep("", "S000001C000002", "S000002"),
//      new NextStep("", "S000001C000003", "S000003")
//    )
//    
//    val step = new DefaultStep("S000001", "nameToShow", "", nextStep, null, null, components, Nil)
    
    
//    Admin.setStep(adminId, true, step, "first")
  }
  
//  def step2(adminId: String) = {
//        
//    /*
//     * create 2. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000002C000001", ""),
//        new ImmutableComponent("S000002C000002", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000002C000001", "S000003"),
//      new NextStep("", "S000002C000002", "S000003")
//    )
//    
//    val step = new DefaultStep("S000002", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "default")
//  }
//  
//  def step3(adminId: String) = {
//        
//    /*
//     * create 3. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000003C000001", ""),
//        new ImmutableComponent("S000003C000002", ""),
//        new ImmutableComponent("S000003C000003", ""),
//        new ImmutableComponent("S000003C000004", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000003C000001", "S000004"),
//      new NextStep("", "S000003C000002", "S000004"),
//      new NextStep("", "S000003C000003", "S000004"),
//      new NextStep("", "S000003C000004", "S000004")
//    )
//    
//    val step = new DefaultStep("S000003", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "default")
//  }
//  
//  def step4(adminId: String) = {
//        
//    /*
//     * create 4. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000004C000001", ""),
//        new ImmutableComponent("S000004C000002", ""),
//        new ImmutableComponent("S000004C000003", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000004C000001", "S000005"),
//      new NextStep("", "S000004C000002", "S000005"),
//      new NextStep("", "S000004C000003", "S000006")
//    )
//    
//    val step = new DefaultStep("S000004", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "default")
//  }
//  
//  def step5(adminId: String) = {
//        
//    /*
//     * create 5. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000005C000001", ""),
//        new ImmutableComponent("S000005C000002", ""),
//        new ImmutableComponent("S000005C000003", ""),
//        new ImmutableComponent("S000005C000004", ""),
//        new ImmutableComponent("S000005C000005", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000005C000001", "S000007"),
//      new NextStep("", "S000005C000002", "S000007"),
//      new NextStep("", "S000005C000003", "S000007"),
//      new NextStep("", "S000005C000004", "S000008"),
//      new NextStep("", "S000005C000005", "S000008")
//    )
//    
//    val step = new DefaultStep("S000005", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "default")
//  }
//  
//  def step6(adminId: String) = {
//        
//    /*
//     * create 6. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000006C000001", ""),
//        new ImmutableComponent("S000006C000002", ""),
//        new ImmutableComponent("S000006C000003", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000006C000001", "S000000"),
//      new NextStep("", "S000006C000002", "S000000"),
//      new NextStep("", "S000006C000003", "S000000")
//    )
//    
//    val step = new DefaultStep("S000006", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "final")
//  }
//  
//  def step7(adminId: String) = {
//        
//    /*
//     * create 7. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000007C000001", ""),
//        new ImmutableComponent("S000007C000002", ""),
//        new ImmutableComponent("S000007C000003", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000007C000001", "S000000"),
//      new NextStep("", "S000007C000002", "S000000"),
//      new NextStep("", "S000007C000003", "S000000")
//    )
//    
//    val step = new DefaultStep("S000007", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "final")
//  }
//  
//  def step8(adminId: String) = {
//        
//    /*
//     * create 8. Step
//     */
//    val components = List[Component](
//        new ImmutableComponent("S000008C000001", ""),
//        new ImmutableComponent("S000008C000002", "")
//    )
//    
//    val nextStep = List[NextStep] (
//      new NextStep("", "S000008C000001", "S000000"),
//      new NextStep("", "S000008C000002", "S000000")
//    )
//    
//    val step = new DefaultStep("S000008", "nameToShow", "", nextStep, null, null, components, Nil)
//    
//    
//    Admin.setStep(adminId, false, step, "final")
//  }
}