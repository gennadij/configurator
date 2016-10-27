/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 */

package org.main

import scala.collection.JavaConversions._

import org.admin.Admin
import org.configTree.step.SelectionCriterium
import org.admin.configTree.AdminStep
import org.admin.configTree.AdminComponent
import org.admin.configTree.AdminConfigTree
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.record.impl.ODocument


class TestScenario9 {
  
  def scenario9_1() = {
    
    
    
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
    
    configTree()
  }
  
  def configTree() = {
    
//    val register = Admin.register("test4", "test4")
    
    val adminId: String = Admin.authenticate("test4", "test4")
    
    println("AdminId : " + adminId)
    
    /*
     * Server --> Client
     */
    val configTree: AdminConfigTree = Admin.configTree(adminId)
    
    configTree.steps.foreach(s =>{
      println(s.stepId)
      println(s.kind)
      s.components.foreach (c => {
        println(c)
      })
    })
    

     //      Server <-- Client
    val step1 = new AdminStep(false, "","",adminId, "first", null)
     //      Server --> Client
    val addedStep1 = Admin.addStep(step1)
    // TODO einzelne Componente mit Step verbinden
//    val addedComponents1 = Admin.addHasComponent(adminId, addedStep1.id, components1)
     //      Server <-- Client
    val c11 = new AdminComponent(false, "", "", adminId, "immutable")
     //      Server --> Client
    val addedComponent11 = Admin.addComponent(c11)
     //      Server <-- Client
    val c12 = new AdminComponent(false, "", "", adminId, "immutable")
     //      Server --> Client
    val addedComponent12 = Admin.addComponent(c12)
     //      Server --> Client
    val c13 = new AdminComponent(false, "", "", adminId, "immutable")
     //      Server <-- Client
    val addedComponent13 = Admin.addComponent(c13)
    
    
    
    
    
    
    val addedNextStep2 = Admin.addStep(new AdminStep(false, "","",adminId, "first", null))
    val addedNextStep3 = Admin.addStep(new AdminStep(false, "","",adminId, "first", null))
    
    val components1 = List(addedComponent11.id, addedComponent12.id, addedComponent13.id)
    
    //TODO umstellen auf das einezelne Component
    
//    val addedComponents1 = Admin.addHasComponent(adminId, addedStep1.id, components1)
    
    val addedStep2 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent21 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent22 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val components2 = List(addedComponent21.id, addedComponent22.id)
    val addedComponents2 = Admin.addHasComponent(adminId, addedStep2.id, components2)
    
    
    val addedStep3 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent31 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent32 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent33 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "mutable"))
    val addedComponent34 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "mutable"))
    val components3 = List(addedComponent31.id, addedComponent32.id, addedComponent33.id, addedComponent34.id)
    val addedComponents3 = Admin.addHasComponent(adminId, addedStep3.id, components3)
    
    
    val addedStep4 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent41 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent42 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent43 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val components4 = List(addedComponent41.id, addedComponent42.id, addedComponent43.id)
    val addedComponents4 = Admin.addHasComponent(adminId, addedStep4.id, components4)
    
    
    val addedStep5 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent51 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent52 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent53 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent54 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent55 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val components5 = List(addedComponent51.id, addedComponent52.id, addedComponent53.id, addedComponent54.id, addedComponent55.id)
    val addedComponents5 = Admin.addHasComponent(adminId, addedStep5.id, components5)
    
    
    val addedStep6 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent61 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent62 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent63 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val components6 = List(addedComponent61.id, addedComponent62.id, addedComponent63.id)
    val addedComponents6 = Admin.addHasComponent(adminId, addedStep6.id, components6)
    
    val addedStep7 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent71 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent72 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent73 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val components7 = List(addedComponent71.id, addedComponent72.id, addedComponent73.id)
    val addedComponents7 = Admin.addHasComponent(adminId, addedStep7.id, components7)
    
    
    val addedStep8 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
    val addedComponent81 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val addedComponent82 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
    val components8 = List(addedComponent81.id, addedComponent82.id)
    val addedComponents8 = Admin.addHasComponent(adminId, addedStep8.id, components8)
    
//    val configTree2: AdminConfigTree = Admin.configTree(adminId)
//    
//    configTree2.steps.foreach(s =>{
//      println(s.stepId)
//      println(s.kind)
//      s.components.foreach (c => {
//        println(c)
//      })
//    })
//    configTree2.steps.last.components.foreach (c => {
//      Admin.addNextStep(adminId, configTree2.steps.last.id, c.id)
//    })
//    
//    val configTree3: AdminConfigTree = Admin.configTree(adminId)
//    
//    configTree3.steps.foreach(s =>{
//      println(s.stepId)
//      println(s.kind)
//      s.components.foreach (c => {
//        println(c)
//      })
//    })
    
    
    
  }
  
  
  def step1(adminId: String) = {
        
    /*
     * create 1. Step
     */
    
//    val step = new AdminStep("", adminId, "first", new SelectionCriterium("1", "1"))
//    
//    val stepId = Admin.addStep(step)
    
//    println(stepId)
    
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