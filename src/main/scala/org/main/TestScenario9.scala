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
import org.admin.configTree.AdminNextStep
import play.api.libs.json._
import org.admin.AdminWeb

class TestScenario9 extends AdminWeb{
  
  
  
  def scenario9_1() = {
    
    //Server ---->> Client
    //Server liefert die Webseite zum Anmelden oder Registrieren
    
    //Server <<---- Client
    //Client liefert Username und Password
    val userNamePasswordJson = Json.obj(
        "jsonId" -> 2, 
        "method" -> "autheticate"
        ,"params" -> Json.obj(
             "username" -> "test8", 
             "password" -> "test8"))
    
    //Server ---->> Client
    val userAdminJson: JsValue = handelMessage(userNamePasswordJson)
    println("Server ---->> Client == " + userAdminJson)
    
    //Server <<---- Client
    // get current ConfigTree
    
    val configTreeJsonClient1 = Json.obj(
        "jsonId" -> 3,
        "method" -> "configTree"
        ,"params"-> Json.obj(
            "adminId" -> (userAdminJson \ "result" \ "id").toString(), 
            "authentication" -> true))

     //Server ---->> Client
     val configTreeJsonServer1 = handelMessage(configTreeJsonClient1)    
  }
  
  
  def scenario9_2() = {
    configTree()
  }
  
  
  

  
  def configTree() = {
    
    val idPassword = "test3"
//    val register = Admin.register(idPassword, idPassword)
    
    val adminId: String = Admin.authenticate(idPassword, idPassword)
    
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
    
    println(Json.toJson(configTree))
    
    //TODO überlegen ob der aktuelle Element aktualisiert wird oder gesamte ConfigTree

//     //      Server <-- Client
//    val step1 = new AdminStep(false, "","",adminId, "first", null)
//     //      Server --> Client
//    val vStep1 = Admin.addStep(step1)
//    println(vStep1)
//     //      Server <-- Client
//    val c11 = new AdminComponent(false, "", "", adminId, "immutable")
//     //      Server --> Client
//    val vComponent11 = Admin.addComponent(c11)
//    val eStep1Component1 = Admin.addHasComponent(adminId, vStep1.id, vComponent11.id)
//    println(vComponent11)
//     //      Server <-- Client
//    val c12 = new AdminComponent(false, "", "", adminId, "immutable")
//    //      Server --> Client
//    val vComponent12 = Admin.addComponent(c12)
//    val eStep1Component2 = Admin.addHasComponent(adminId, vStep1.id, vComponent12.id)
//    println(vComponent12)
//    //      Server --> Client
//    val c13 = new AdminComponent(false, "", "", adminId, "immutable")
//    //      Server <-- Client
//    val vComponent13 = Admin.addComponent(c13)
//    val eStep1Component3 = Admin.addHasComponent(adminId, vStep1.id, vComponent13.id)
//    println(vComponent13)
//    
//    
//    //      Server <-- Client (add NextStep)
//    val nextStep1 = new AdminStep(false, "", "", adminId, "default", null)
//    println(nextStep1)
//    val componentId1 = vComponent11.id
//    println(nextStep1 + "  " + componentId1)
//    //      Server --> Client
//    val vNextStep1 = Admin.addStep(nextStep1)
//    val eNextStep1 = Admin.addNextStep(adminId, componentId1, vNextStep1.id)
//    println(vNextStep1)
//    //      Server <-- Client (add NextStep)
//    val nextStep2 = new AdminStep(false, vNextStep1.id, vNextStep1.adminId, adminId, "default", null)
//    val componentId2 = vComponent12.id
//    println(nextStep2 + "  " + componentId2)
//    //      Server --> Client
//    if(nextStep2.id != "" && nextStep2.stepId != ""){
//      val eNextStep2 = Admin.addNextStep(adminId, componentId2, nextStep2.id)
//      println(eNextStep2)
//    }else{
//      println("add new Step")
//      val vNextStep2 = Admin.addStep(nextStep2)
//      val eNextStep2 = Admin.addNextStep(adminId, componentId2, vNextStep2.id)
//      println(vNextStep2 + "   " + eNextStep2)
//    }
//    //      Server <-- Client (add NextStep)
//    val nextStep3 = new AdminStep(false, "", "", adminId, "default", null)
//    val componentId3 = vComponent13.id
//    println(nextStep3 + "  " + componentId3)
//    //      Server --> Client
//    if(nextStep3.id != "" && nextStep3.adminId != ""){
//      val eNextStep3 = Admin.addNextStep(adminId, componentId3, vNextStep1.id)
//      println(eNextStep3)
//    }else{
//      println("add new Step")
//      val vNextStep3 = Admin.addStep(nextStep3)
//      val eNextStep3 = Admin.addNextStep(adminId, componentId3, vNextStep3.id)
//      println(vNextStep3 + "   " + eNextStep3)
//    }
    
    
//    val addedNextStep3 = Admin.addStep(new AdminStep(false, "","",adminId, "first", null))
//    
//    val addedStep2 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent21 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent22 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val components2 = List(addedComponent21.id, addedComponent22.id)
//    val addedComponents2 = Admin.addHasComponent(adminId, addedStep2.id, components2)
//    
//    
//    val addedStep3 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent31 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent32 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent33 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "mutable"))
//    val addedComponent34 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "mutable"))
//    val components3 = List(addedComponent31.id, addedComponent32.id, addedComponent33.id, addedComponent34.id)
//    val addedComponents3 = Admin.addHasComponent(adminId, addedStep3.id, components3)
//    
//    
//    val addedStep4 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent41 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent42 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent43 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val components4 = List(addedComponent41.id, addedComponent42.id, addedComponent43.id)
//    val addedComponents4 = Admin.addHasComponent(adminId, addedStep4.id, components4)
//    
//    
//    val addedStep5 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent51 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent52 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent53 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent54 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent55 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val components5 = List(addedComponent51.id, addedComponent52.id, addedComponent53.id, addedComponent54.id, addedComponent55.id)
//    val addedComponents5 = Admin.addHasComponent(adminId, addedStep5.id, components5)
//    
//    
//    val addedStep6 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent61 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent62 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent63 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val components6 = List(addedComponent61.id, addedComponent62.id, addedComponent63.id)
//    val addedComponents6 = Admin.addHasComponent(adminId, addedStep6.id, components6)
//    
//    val addedStep7 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent71 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent72 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent73 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val components7 = List(addedComponent71.id, addedComponent72.id, addedComponent73.id)
//    val addedComponents7 = Admin.addHasComponent(adminId, addedStep7.id, components7)
//    
//    
//    val addedStep8 = Admin.addStep(new AdminStep(false, "","",adminId, "default", null))
//    val addedComponent81 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val addedComponent82 = Admin.addComponent(new AdminComponent(false, "", "", adminId, "immutable"))
//    val components8 = List(addedComponent81.id, addedComponent82.id)
//    val addedComponents8 = Admin.addHasComponent(adminId, addedStep8.id, components8)
    
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