package models.persistence.orientdb

import scala.collection.JavaConverters._
import models.wrapper.component.ComponentIn
import models.wrapper.component.ComponentOut
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import models.persistence.OrientDB
import models.status.Status
import play.api.Logger
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import models.wrapper.dependency.Dependency
import com.tinkerpop.blueprints.impls.orient.OrientEdge
import com.tinkerpop.blueprints.Direction
import models.wrapper.common.SelectionCriterium
import models.currentConfig.CurrentConfig
import models.bo.StepCurrentConfigBO
import models.wrapper.common.Component
import models.json.component.JsonComponentOut
import models.status.component.StatusComponent
import models.status.component.RequireComponent
import models.status.component.AllowNextComponent
import models.status.component.ExcludeComponent
import models.status.component.StatusSelectedComponent
import models.status.component.ExcludedComponent
import models.status.component.NotExcludedComponent
import models.status.component.StatusExcludeDependency
import models.status.component.AddedComponent
import models.status.component.RemovedComponent
import models.status.component.RequireNextStep
import models.status.FinalComponent
import models.status.component.StatusSelectionCriterium
import models.status.ClassCastError
import models.status.ODBReadError
import models.status.ClassCastError
import models.status.Status
import models.status.component.StatusSelectionCriterium
import models.status.Status
import models.status.Status
import models.status.Success
import models.status.component.ErrorComponent
import models.status.Error
import models.bo.ComponentBO
import models.bo.StepBO
import models.bo.StepBO
import models.bo.DependencyBO

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann on 13.11.2017
 */
object ComponentVertex {
  
    /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param List[OrientVertex]
   * 
   * @return OrientVertex
   */
  def getFatherStep(componentId: String): Option[StepBO] = {
    
    val graph: OrientGraph = OrientDB.getFactory().getTx
    
    val vFatherStep: Option[OrientVertex] = try{
      val vComponent = graph.getVertex(componentId)
      
      val eHasComponents: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_COMPONENT)
          .asScala.toList map {_.asInstanceOf[OrientEdge]}
      
      val vFatherStep: List[OrientVertex] = eHasComponents map {
        eHasComponent => {
          eHasComponent.getVertex(Direction.OUT)
        }
      }
      
      Some(vFatherStep.head)
    
    }catch{
      //TODO richtigen Objekt fuer Error im Datenbank
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
//        val errorStatus = StatusComponent(None, None, None, Some(ClassCastError()), None)
//        createExceptionComponentOut(errorStatus)
        None
      }
      case e1: Exception => {
        graph.rollback()
//        val errorStatus = StatusComponent(None, None, None, Some(ODBReadError()), None)
        Logger.error(e1.printStackTrace().toString)
//        createExceptionComponentOut(errorStatus)
        None
      }
    }
    
    vFatherStep match {
      case Some(vFatherStep) => {
        Some(StepBO(
            vFatherStep.getIdentity.toString,
            vFatherStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
            vFatherStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt,
            vFatherStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt
        ))
      }
      case None => None
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param ComponentIn
   * 
   * @return ComponentOut
   */
  def getComponent(componentId: String): Option[ComponentBO] = {
    
    val graph: OrientGraph = OrientDB.getFactory().getTx
    
    val vComponent: Option[OrientVertex] = try{
      
      Some(graph.getVertex(componentId))
      
    }catch{
      //TODO richtigen Objekt fuer Error im Datenbank
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
//        val errorStatus = StatusComponent(None, None, None, Some(ClassCastError()), None)
//        createExceptionComponentOut(errorStatus)
        None
      }
      case e1: Exception => {
        graph.rollback()
//        val errorStatus = StatusComponent(None, None, None, Some(ODBReadError()), None)
        Logger.error(e1.printStackTrace().toString)
//        createExceptionComponentOut(errorStatus)
        None
      }
    }
    
    vComponent match {
      case Some(vComponent) => {
        Some(ComponentBO(
            vComponent.getIdentity.toString,
            vComponent.getProperty(PropertyKeys.NAME_TO_SHOW),
            getComponentDependenciesOut_v002(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE},
            getComponentDependenciesIn_v002(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE},
            getComponentDependenciesOut_v002(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE},
            getComponentDependenciesIn_v002(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE}
        ))
      }
      case None => None
    }
  }
  
  def getNextStep(componentId: String): Option[StepBO] = {
    val graph: OrientGraph = OrientDB.getFactory().getTx
    
    val vNextStep: Option[OrientVertex] = try{
    
      graph.getVertex(componentId).getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList match {
        case eHasSteps if eHasSteps.size == 1 => Some(eHasSteps.head.getVertex(Direction.IN).asInstanceOf[OrientVertex])
//        case eHasSteps if eHasSteps.size > 1 => //TODO Error -> es kann 2 nextStep exsistieren
        case eHasSteps if eHasSteps.size == 0 => None
      }
    }catch{
      //TODO richtigen Objekt fuer Error im Datenbank
      case e2 : ClassCastException => {
        graph.rollback()
        Logger.error(e2.printStackTrace().toString)
//        val errorStatus = StatusComponent(None, None, None, Some(ClassCastError()), None)
//        createExceptionComponentOut(errorStatus)
        None
      }
      case e1: Exception => {
        graph.rollback()
//        val errorStatus = StatusComponent(None, None, None, Some(ODBReadError()), None)
        Logger.error(e1.printStackTrace().toString)
//        createExceptionComponentOut(errorStatus)
        None
      }
    }
    
    
    vNextStep match {
      case Some(vNextStep) => {
        Some(StepBO(
            vNextStep.getIdentity.toString,
            vNextStep.getProperty(PropertyKeys.NAME_TO_SHOW).toString,
            vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt,
            vNextStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt
        ))
      }
      case None => None
    }
  }
  
  
//  def component(componentIn: ComponentIn): ComponentOut = {
//    val graph: OrientGraph = OrientDB.getFactory().getTx
//    
//    try{
//      
//      val vComponent = graph.getVertex(componentIn.componentId)
//      
//      val excludeDependenciesOut: List[Dependency] = getComponentDependenciesOut(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE}
//      
////      Logger.info(this.getClass.getSimpleName + " excludeDependenciesOut : " + excludeDependenciesOut)
//      
//      val requireDependenciesOut: List[Dependency] = getComponentDependenciesOut(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE}
//      
////      Logger.info(this.getClass.getSimpleName + " requireDependenciesOut : " + requireDependenciesOut)
//      
//      val excludeDependenciesIn: List[Dependency] = getComponentDependenciesIn(vComponent) filter {_.dependencyType == PropertyKeys.EXCLUDE}
//      
////      Logger.info(this.getClass.getSimpleName + " excludeDependenciesIn : " + excludeDependenciesIn)
//      
//      val requireDependenciesIn: List[Dependency] = getComponentDependenciesIn(vComponent) filter {_.dependencyType == PropertyKeys.REQUIRE}
//      
////      Logger.info(this.getClass.getSimpleName + " requireDependenciesIn : " + requireDependenciesIn)
//      
//      val vFatherStep: OrientVertex = getFatherStep(vComponent)
//      
////      Logger.info(this.getClass.getSimpleName + ": fatherStep " + vFatherStep)
//      
//      val selectionCriterium: SelectionCriterium = getSelectionCriterium(vFatherStep)
//      
////      Logger.info(this.getClass.getSimpleName + ": selectionCriterium " + selectionCriterium)
//      
//      val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(vFatherStep.getIdentity.toString)
//      
////      Logger.info(this.getClass.getSimpleName + ": currentStep from CurrentConfig " + currentStep.get.getClass.hashCode())
//
//      val statusSelectedComponent: StatusSelectedComponent = checkSelectedComponent(currentStep, componentIn.componentId)
//      
//      val previousSelectedComponents: List[Component] = currentStep match {
//        case Some(step) => step.components
//        case None => List()
//      }
//     
////      Logger.info(this.getClass.getSimpleName + ": previousSelectedComponents " + previousSelectedComponents)
//      
//      val stausSelectionCriterium: StatusSelectionCriterium = 
//          checkSelectionCriterium(previousSelectedComponents.size, selectionCriterium, statusSelectedComponent)
//      
////      Logger.info(this.getClass.getSimpleName + ": " + stausSelectionCriterium)
//      
//      val statusExcludeDependencies: StatusExcludeDependency = checkExcludeDependencies(currentStep.get, excludeDependenciesIn)
//      
//      val nextStepExistence: Boolean = checkNextStepExistence(vComponent)
//      
//      val commonStatus: Status = Success()
//      
//      val status: StatusComponent = statusExcludeDependencies match {
//        case NotExcludedComponent() => {
//          StatusComponent(
//            Some(stausSelectionCriterium), 
//            Some(statusSelectedComponent), 
//            Some(statusExcludeDependencies), 
//            Some(commonStatus), 
//            Some(nextStepExistence))
//        }
//        case ExcludedComponent() => {
//          StatusComponent(
//            Some(stausSelectionCriterium), 
//            Some(ErrorComponent()), 
//            Some(statusExcludeDependencies), 
//            Some(Error()), 
//            Some(nextStepExistence))
//        }
//      }
//      
//      val dependencies: List[Dependency] = requireDependenciesOut ::: excludeDependenciesOut
//      
////      Logger.info(this.getClass.getSimpleName + " Status : " + status)
//      
//      createComponentOut(
//          status, 
//          currentStep, 
//          vComponent.getIdentity.toString, 
//          vComponent.getProperty(PropertyKeys.NAME_TO_SHOW), 
//          vFatherStep.getIdentity.toString, 
//          dependencies)
//      
//    }catch{
//      case e2 : ClassCastException => {
//        graph.rollback()
//        Logger.error(e2.printStackTrace().toString)
//        val errorStatus = StatusComponent(None, None, None, Some(ClassCastError()), None)
//        createExceptionComponentOut(errorStatus)
//      }
//      case e1: Exception => {
//        graph.rollback()
//        val errorStatus = StatusComponent(None, None, None, Some(ODBReadError()), None)
//        Logger.error(e1.printStackTrace().toString)
//        createExceptionComponentOut(errorStatus)
//      }
//    }
//  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.2
//   * 
//   * @param StatusComponent
//   * 
//   * @return ComponentOut
//   */
//  
//  def createComponentOut(
//      status: StatusComponent, 
//      currentStep: Option[StepCurrentConfigBO],
//      componentId: String, 
//      componentNameToShow: String,
//      fatherStepId: String, 
//      dependencies: List[Dependency]): ComponentOut = {
//    
//    status.excludeDependency.get match {
//      case statusExcludedComponent: ExcludedComponent => {
//        ComponentOut(  
//            componentId,
//            fatherStepId,
//            status,
//            List()
//         )
//      }
//      case statusNotExcludedComponent: NotExcludedComponent => {
//        defineStatusForSelectionCreterium(status, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
//      }
//    }
//  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.2
//   * 
//   * @param StatusComponent
//   * 
//   * @return ComponentOut
//   */
//  
//  def checkSelectedComponent(currentStep: Option[StepCurrentConfigBO], componentInId: String): StatusSelectedComponent = {
//    currentStep match {
//      case Some(step) => {
//        step.components.exists(_.componentId == componentInId) match {
//          case true => {
//            CurrentConfig.removeComponent(currentStep.get.stepId, componentInId)
//            RemovedComponent()
//          }
//          case false => AddedComponent()
//        }
//      }
//      case None => AddedComponent()
//    }
//  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.2
//   * 
//   * @param 
//   * 
//   * @return ComponentOut
//   */
//  def defineStatusForSelectionCreterium(
//      status: StatusComponent, 
//      currentStep: Option[StepCurrentConfigBO],
//      componentId: String,
//      componentNameToShow: String,
//      fatherStepId: String,
//      dependencies: List[Dependency]): ComponentOut = {
//    status.selectionCriterium.get match {
//      case statusRequireComponent:   RequireComponent => {
//        
//        defineStatusSelectedComponent(status, statusRequireComponent, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
//      }
//      case statusRequireNextStep:    RequireNextStep => {
//        
//        defineStatusSelectedComponent(status, statusRequireNextStep, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
//        
//      }
//      case statusAllowNextComponent: AllowNextComponent => {
//        defineStatusSelectedComponent(status, statusAllowNextComponent, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
//      }
//      case statusExcludeComponent:   ExcludeComponent => {
//        CurrentConfig.printCurrentConfig
//        ComponentOut(
//            componentId,
//            fatherStepId,
//            status,
//            dependencies
//         )
//      }
//    }
//  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.2
//   * 
//   * @param 
//   * 
//   * @return ComponentOut
//   */
//  def defineStatusSelectedComponent(
//      status: StatusComponent,
//      statusSelectionCriterium: StatusSelectionCriterium,
//      currentStep: Option[StepCurrentConfigBO],
//      componentId: String,
//      componentNameToShow: String,
//      fatherStepId: String,
//      dependencies: List[DependencyBO]): ComponentOut = {
//    status.selectedComponent.get match {
//      case statusErrorComponent: ErrorComponent => {
//        ComponentOut(
//            "",
//            "",
//            status,
//            List()
//        )
//      }
//      case statusRemoveComponent: RemovedComponent => {
//        ComponentOut(
//            componentId,
//            fatherStepId,
//            status,
//            dependencies
//         )
//      }
//      case statusAddComponent: AddedComponent => {
//        val component: Component = Component(
//            componentId,
//            componentNameToShow
//        )
//        CurrentConfig.addComponent(currentStep.get, component)
//        CurrentConfig.printCurrentConfig
//        status.nextStepExistence.get match {
//          case true => {
//            ComponentOut(
//                componentId,
//                fatherStepId,
//                status,
//                dependencies
//            )
//          }
//          case false => {
//            val finalStepStatus = status.copy(common = Some(FinalComponent()))
//            ComponentOut(
//                componentId,
//                fatherStepId,
//                status, //TODO Final Step Status fehlt
//                dependencies
//            )
//          }
//        }
//      }
//    }
//  }
//  
//  def createExceptionComponentOut(status: StatusComponent): ComponentOut = {
//    ComponentOut(
//            "",
//            "",
//            status,
//            List()
//        )
//  }
//  
//   /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.1
//   * 
//   * @param OrientVertex
//   * 
//   * @return List[Dependency]
//   */
//  def getComponentDependenciesIn(vComponent: OrientVertex): List[Dependency] = {
//    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_DEPENDENCY)
//        .asScala.toList map {_.asInstanceOf[OrientEdge]}
//    eHasDependencies map {
//      eHasDependency => {
//        //TODO PropertyKey.VISUALIZATION in DB mit Leerzeichen
//        Dependency(
//            eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
//            eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
//            eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
//            eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
//            eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString  //nameToShow: String
//        )
//      }
//    }
//  }
//  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param OrientVertex
   * 
   * @return List[DependencyBO]
   */
  def getComponentDependenciesIn_v002(vComponent: OrientVertex): List[DependencyBO] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.IN, PropertyKeys.HAS_DEPENDENCY)
        .asScala.toList map {_.asInstanceOf[OrientEdge]}
    eHasDependencies map {
      eHasDependency => {
        //TODO PropertyKey.VISUALIZATION in DB mit Leerzeichen
        DependencyBO(
            eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
            eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
            eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
            eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
            eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString  //nameToShow: String
        )
      }
    }
  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.1
//   * 
//   * @param OrientVertex
//   * 
//   * @return List[Dependency]
//   */
//  def getComponentDependenciesOut(vComponent: OrientVertex): List[Dependency] = {
//    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.OUT, PropertyKeys.HAS_DEPENDENCY)
//        .asScala.toList map {_.asInstanceOf[OrientEdge]}
//    eHasDependencies map {
//      eHasDependency => {
//        //TODO PropertyKey.VISUALIZATION in DB mit Leerzeichen
//        Dependency(
//            eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
//            eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
//            eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
//            eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
//            eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString  //nameToShow: String
//        )
//      }
//    }
//  }
//  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param OrientVertex
   * 
   * @return List[DependencyBO]
   */
  def getComponentDependenciesOut_v002(vComponent: OrientVertex): List[DependencyBO] = {
    val eHasDependencies: List[OrientEdge] = vComponent.getEdges(Direction.OUT, PropertyKeys.HAS_DEPENDENCY)
        .asScala.toList map {_.asInstanceOf[OrientEdge]}
    eHasDependencies map {
      eHasDependency => {
        DependencyBO(
            eHasDependency.getProperty(PropertyKeys.OUT).asInstanceOf[OrientVertex].getIdentity.toString, //outId: String,
            eHasDependency.getProperty(PropertyKeys.IN).asInstanceOf[OrientVertex].getIdentity.toString, //inId: String,
            eHasDependency.getProperty(PropertyKeys.VISUALIZATION).toString, //visualization: String,
            eHasDependency.getProperty(PropertyKeys.DEPENDENCY_TYPE).toString, //dependencyType: String,
            eHasDependency.getProperty(PropertyKeys.NAME_TO_SHOW).toString  //nameToShow: String
        )
      }
    }
  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.1
//   * 
//   * @param List[OrientVertex]
//   * 
//   * @return OrientVertex
//   */
//  def getFatherStep(component: OrientVertex): OrientVertex = {
//    val eHasComponents: List[OrientEdge] = component.getEdges(Direction.IN, PropertyKeys.HAS_COMPONENT)
//        .asScala.toList map {_.asInstanceOf[OrientEdge]}
//    
//    val vFatherStep: List[OrientVertex] = eHasComponents map {
//      eHasComponent => {
//        eHasComponent.getVertex(Direction.OUT)
//      }
//    }
//    vFatherStep.head
//  }
//  
//
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.1
//   * 
//   * @param List[OrientVertex]
//   * 
//   * @return OrientVertex
//   */
//  def getSelectionCriterium(vStep: OrientVertex): SelectionCriterium = {
//    SelectionCriterium(
//        vStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MIN).toString.toInt, //min: Int,
//        vStep.getProperty(PropertyKeys.SELECTION_CRITERIUM_MAX).toString.toInt //max: Int
//    )
//  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * @version 0.0.1
//   * 
//   * @param 
//   * 
//   * @return
//   */
//  
//  def checkSelectionCriterium(
//      countOfSelectedComponents: Int, 
//      selectionCriterium: SelectionCriterium,
//      statusSelectedComponent: StatusSelectedComponent): StatusSelectionCriterium = {
//    //Ungepruefte Komponente, die noch nicht in der aktuellen Konfiguration hinzugefuegt wird
//    val countOfComponents = statusSelectedComponent match {
//      case AddedComponent() => countOfSelectedComponents + 1
//      case RemovedComponent() => countOfSelectedComponents
//      case ErrorComponent() => countOfSelectedComponents
//    }
//    
//    val min = selectionCriterium.min
//    val max = selectionCriterium.max
//    
////    Logger.info(this.getClass.getSimpleName + " countOfComponents : " + countOfComponents)
////    Logger.info(this.getClass.getSimpleName + " selectionCriterium : " + min + " " + max)
//    
//    selectionCriterium match {
//      case requireComponent if min > countOfComponents && max > countOfComponents => RequireComponent()
//      case requireNextStep if min <= countOfComponents && max == countOfComponents => RequireNextStep()
//      case allowNextComponent if min <= countOfComponents && max > countOfComponents => AllowNextComponent()
//      case excludeComponent if max < countOfComponents => ExcludeComponent()
//    }
//  }
//  
//  /**
//   * @author Gennadi Heimann
//   * 
//   * Prueft ob die ausgewaelte Komponente schliesst eine von der Komponente in der aktuellen Konfiguration aus
//   * 
//   * @version 0.0.1
//   * 
//   * @param 
//   * 
//   * @return
//   */
//  def checkExcludeDependencies(currentStep: StepCurrentConfigBO, inExcludeDependencies: List[Dependency]): StatusExcludeDependency = {
//    val selectedcomponentIds: List[String] = currentStep.components map (_.componentId)
//    val inExcludeComponentIds: List[String] = inExcludeDependencies map (_.outId)
//    
//    val excludeComponentsIds: List[String] = selectedcomponentIds flatMap { sCId => inExcludeComponentIds.filter{inECId => sCId == inECId} }
//    
//    excludeComponentsIds.size match {
//      case count if count > 0 => ExcludedComponent()
//      case count if count == 0 => NotExcludedComponent()
//    }
//  }
//  
//  def checkNextStepExistence(vComponent: OrientVertex): Boolean = {
//    vComponent.getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList match {
//      case eHasSteps if eHasSteps.size > 0 => true
//      case eHasSteps if eHasSteps.size == 0 => false
//    }
//  }
}