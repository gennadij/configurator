package org.persistence


import org.configSettings.ConfigSetting
import org.status.SuccessfulStatus
import org.configTree.step.Step
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable

import scala.collection.JavaConversions._
import com.orientechnologies.orient.core.sql.OCommandSQL
import org.persistence.db.orientdb.StepVertex
import org.persistence.db.orientdb.ComponentVertex
import org.persistence.db.orientdb.HasComponentEdge
import org.persistence.db.orientdb.NextStepEdge
import org.admin.AdminUser
import org.persistence.db.orientdb.AdminUserVertex
import org.admin.configTree.AdminStep
import org.admin.configTree.AdminComponent
import org.persistence.db.orientdb.OrientDB
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Vertex
import org.admin.configTree.AdminNextStep
import org.admin.configTree.AdminConfigTreeStep
import org.admin.configTree.AdminConfigTree

object Persistence {
  
  def rules() = ???
  
  def firstStep = {
  }
  
  def registAdminUser(username: String, password: String): AdminUser = {
    
    AdminUserVertex.create(username, password)
  }
  
  def authenticate(username: String, password: String): String = {
    val adminId: String = AdminUserVertex.adminId(username, password)
    if(adminId.isEmpty()) "" else "AU" + adminId
  }
  
  def addStep(step: AdminStep): String = {
    StepVertex.addStep(step)
  }
  
  def addComponent(component: AdminComponent): String = {
    ComponentVertex.addComponent(component)
  }
  
  /**
   * TODO korrektur fue falsche IDs 
   */
  
  def getConfigTree(adminId: String): AdminConfigTree = {
    val graph: OrientGraph = OrientDB.getGraph
    
    val res: OrientDynaElementIterable = graph
      .command(new OCommandSQL(s"SELECT FROM Step WHERE adminId='$adminId'")).execute()
      
    val vSteps: List[OrientVertex] = res.toList.map(_.asInstanceOf[OrientVertex])
    
    new AdminConfigTree(vSteps.map(getAdminStep(_, graph)))
  }
  
  def getAdminStep(vStep: OrientVertex, graph: OrientGraph): AdminConfigTreeStep = {
      val eHasComponent: List[Edge] = vStep.getEdges(Direction.OUT).toList
      val vComponents: List[Vertex] = eHasComponent.map { hC => hC.getVertex(Direction.IN) }
      
      val stepId = if(vStep.getProperty("stepId").toString().substring(1) == vStep.getId.toString()){
          vStep.getProperty("stepId").toString().substring(1)}
        else {
          vStep.setProperty("stepId", vStep.getId.toString())
          graph.commit()
          "S" + vStep.getProperty("stepId").toString
        }
      
      new AdminConfigTreeStep(
          vStep.getIdentity.toString,
          stepId,
          vStep.getProperty("adminId").toString,
          vStep.getProperty("kind").toString(),
          getAdminComponents(vComponents)
      )
  }
  
  def getNextStep(component: Vertex): String = {
    val eNextStep: List[Edge] = component.getEdges(Direction.OUT).toList
    val vNextStep: List[Vertex] = eNextStep.map ( { eNS => 
      eNS.getVertex(Direction.IN)
    })
    println(vNextStep)
    
    "NS" + vNextStep.head.getId.toString()
  }
  
  def getAdminComponents(vComponents: List[Vertex]): List[AdminComponent] = {
    vComponents.map({ vC => 
        new AdminComponent(
            vC.getId.toString(),
            vC.getProperty("componentId"),
            vC.getProperty("adminId").toString,
            vC.getProperty("kind").toString(),
            getNextStep(vC)
        )
      })
  }
  
  def setStep(adminId: String, isConnected: Boolean, step: Step, kind: String) = {
    
    
    
//    val vStep = new VertexStep(
//        step.id, step.nameToShow, step.fatherStep, step.nextStep,
//        step.components, step.dependencies)
    
    /**
     * 1. Erstelle die Schema 
     * 		Vertex -> Step, Component 
     * 		Edge -> hasComponent, nextStep
     * 
     * 
     * 2. befühle die Classes mit objects
     * 
     * 4. update objects
     * 
     * 5. Step besteht aus der 
     * 		1. HauptStep und deren Components
     * 		2. Components mit deren NextSteps
     * 
     * 6. NextStep werden bei der erzeugzng von der HauptStep erzeugt
     * 
     * 7. Zuerst wird Config erstellt. Der Config bildet nur das Ablauf der Konfiguration ab.
     * 
     * 8. Wenn config erstellt wurde, nachher werden die Regeln hinzugefuegt in der config Graph
     * 
     * 9. Die Rules bestehen aus:
     * 		Edge -> dependency
     */
    
    /*
     * create Step
     * content:
     * - stepId
     * - kind
     */
    if(isConnected){
      val propStep = Map("stepId" -> step.id, "adminId" -> adminId)
      println(StepVertex.create(propStep).message)

    }
    /*
     * create Components
     * content each Component
     * - componentId
     * - adminId
     */
    
    step.components foreach ( c => {
      val propComponent = Map("componentId" -> c.id, "adminId" -> adminId)
      println(ComponentVertex.create(propComponent).message)
    } )
    
    /*
     * create nextSteps
     * content each nextStep
     * - stepId
     * - adminId
     */
    
    step.nextStep foreach ( nS => {
      if(nS.step != "S00000"){
        val propNextStep = Map("stepId" -> nS.step , "adminId" -> adminId)
        println(StepVertex.create(propNextStep).message)
      }
      
    } )

    /*
     * connect Step with Components
     * 
     */
    
    val stStepToComponent = HasComponentEdge.connect(step.id, step.components)
    
    stStepToComponent.foreach { s => println(s.message) }
    /*
     * create NextStep
     */
    
    val stComponentsToNextStep = NextStepEdge.connect(step.nextStep)
    stComponentsToNextStep.foreach { s => println(s.message) }
    
    new SuccessfulStatus("Step created")
  }
  
  
  def getPersisitence = {
    val configSetting = scala.xml.XML.loadFile("persistence/config_settings.xml")
    
    val kindOfpersisitenceForConfigTree = configSetting \ "persistence" \ "persistenceKind" \ "forConfigTree"
    val kindOfpersisitenceForRule = configSetting \ "persistence" \ "persistenceKind" \ "forRule"
    
    val configTree = configSetting \ "persistence" \ "xml" \ "config"
    val rule = configSetting \ "persistence" \ "xml" \ "rule"
    
    new ConfigSetting("", configTree.text.toString(), rule.text.toString(), "presentation")
  }
}


class Persistence {
  
}