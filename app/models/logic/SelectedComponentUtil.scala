package models.logic

import models.status.Status
import models.status.ODBReadError
import models.status.ODBClassCastError
import models.status.Success
import models.status.Error
import models.bo.StepBO
import models.bo.StepCurrentConfigBO
import models.bo.DependencyBO
import models.status.component.StatusExcludeDependency
import models.status.component.ExcludedComponent
import models.status.component.NotExcludedComponent
import models.status.component.StatusSelectedComponent
import models.status.component.StatusSelectionCriterium
import models.status.component.RequireComponent
import models.status.component.RequireNextStep
import models.status.component.AllowNextComponent
import models.status.component.ExcludeComponent
import models.status.component.AddedComponent
import models.status.component.RemovedComponent
import models.status.component.ErrorSelectedComponent
import models.currentConfig.CurrentConfig
import models.wrapper.component.ComponentOut
import models.bo.ComponentBO
import models.status.component.StatusComponent
import models.status.component.NotAllowedComponent

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 14.02.2018
 */

object SelectedComponentUtil {
  
  def checkCommonStatus(commonStatuses: List[Status]): Status = {
    new SelectedComponentUtil().checkCommonStatus(commonStatuses)
  }
  
  def checkNextStepExistence(nextStep: Option[StepBO]): Boolean = {
    new SelectedComponentUtil().checkNextStepExistence(nextStep)
  }
  
  def checkExcludeDependencies(
      previousSelectedComponentIds: List[String], 
      inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
    new SelectedComponentUtil().checkExcludeDependencies(previousSelectedComponentIds, inExcludeDependencies)
  }
  
  def checkSelectionCriterium(
      countOfSelectedComponents: Int, 
      fatherStep: StepBO,
      statusSelectedComponent: StatusSelectedComponent,
      selectedComponentBO: ComponentBO): StatusSelectionCriterium = {
    new SelectedComponentUtil().checkSelectionCriterium(
        countOfSelectedComponents, 
        fatherStep, statusSelectedComponent, selectedComponentBO)
  }
  
  def checkSelectedComponent(
      statusExcludeDependency: StatusExcludeDependency,
      currentStep: StepCurrentConfigBO, 
      componentInId: String): StatusSelectedComponent = {
    new SelectedComponentUtil().checkSelectedComponent(statusExcludeDependency, currentStep, componentInId)
  }
  
  def defineStatusForSelectionCreterium(
      status: StatusComponent, 
      currentStep: Option[StepCurrentConfigBO],
      componentId: String,
      componentNameToShow: String,
      fatherStepId: String,
      dependencies: List[DependencyBO]): ComponentOut = {
    new SelectedComponentUtil().defineStatusForSelectionCreterium(
        status, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
  }
}

class SelectedComponentUtil {
  /**
   * @author Gennadi Heimann
   * 
   * Priority of Status
   * 
   * 1. ODBReadError
   * 
   * 2. ClassCastError
   * 
   * 3. Error
   * 
   * 4. Success
   * 
   * @version 0.0.2
   * 
   * @param List[Status]
   * 
   * @return Status
   */
  private def checkCommonStatus(commonStatuses: List[Status]): Status = {
    
    val oDBReadErrorStatuses: List[Status] = commonStatuses.filter(_.isInstanceOf[ODBReadError])
    
    val classCastErrorStatus: List[Status] = commonStatuses.filter(_.isInstanceOf[ODBClassCastError])
    
    val errorStatus: List[Status] = commonStatuses.filter(_.isInstanceOf[Error])
    
    val successStatus: List[Status] = commonStatuses.filter(_.isInstanceOf[Success])
    
    def checkclassCastErrorStatus(classCastErrorStatus: List[Status]): Status = classCastErrorStatus match {
      case List() => checkErrorStatus(errorStatus)
      case _ => ODBClassCastError()
    }
    
    def checkErrorStatus(errorStatus: List[Status]): Status = errorStatus match {
      case List() => checkSuccessStatus(successStatus)
      case _ => Error()
    }
    
    def checkSuccessStatus(successStatus: List[Status]): Status = successStatus match {
      case List() => Error()
      case _ => Success()
    }
    
    oDBReadErrorStatuses match {
      case List() => checkclassCastErrorStatus(classCastErrorStatus)
      case _ => ODBReadError()
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return
   */
  private def checkNextStepExistence(nextStep: Option[StepBO]): Boolean = {
    nextStep match {
      case Some(nextStep) => true
      case None => false
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * Prueft ob die ausgewaelte Komponente schliesst eine von der Komponente in der aktuellen Konfiguration aus
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return
   */
  private def checkExcludeDependencies(
      previousSelectedComponentIds: List[String], 
      inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
//    val selectedcomponentIds: List[String] = currentStep.components map (_.componentId)
    val inExcludeComponentIds: List[String] = inExcludeDependencies map (_.outId)
    
    val excludeComponentsIds: List[String] = previousSelectedComponentIds flatMap { sCId => inExcludeComponentIds.filter{inECId => sCId == inECId} }
    
    excludeComponentsIds.size match {
      case count if count > 0 => ExcludedComponent()
      case count if count == 0 => NotExcludedComponent()
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return
   */
  
  private def checkSelectionCriterium(
      countOfSelectedComponents: Int, 
      fatherStep: StepBO,
      statusSelectedComponent: StatusSelectedComponent,
      selectedComponentBO: ComponentBO): StatusSelectionCriterium = {
    //Ungepruefte Komponente, die noch nicht in der aktuellen Konfiguration hinzugefuegt wird
    
    val countOfDependenciesIn: Int = selectedComponentBO.excludeDependenciesIn.size
    
    val countOfSiblingComponents: Int = fatherStep.componentIds.size - 1
    
    val countOfComponents: Int = fatherStep.componentIds.size
    
    val countOfComponentsInCurrentConfig = statusSelectedComponent match {
      case AddedComponent() => countOfSelectedComponents + 1
      case RemovedComponent() => countOfSelectedComponents - 1
      case ErrorSelectedComponent() => countOfSelectedComponents
      case NotAllowedComponent() => countOfSelectedComponents
    }
    
    val min: Int = fatherStep.selectionCriteriumMin
    val max: Int = fatherStep.selectionCriteriumMax
    
    println("countOfDependenciesIn " + countOfDependenciesIn)
    println("countOfSiblingComponents " + countOfSiblingComponents)
    println("countOfComponents " + countOfComponents)
    println("countOfSelectedComponents " + countOfSelectedComponents)
    println("countOfComponentsInCurrentConfig " + countOfComponentsInCurrentConfig)
    
//    Logger.info(this.getClass.getSimpleName + " countOfComponents : " + countOfComponents)
//    Logger.info(this.getClass.getSimpleName + " selectionCriterium : " + min + " " + max)
    if((countOfDependenciesIn + countOfComponentsInCurrentConfig) == countOfComponents) RequireNextStep()
    else {
      if(min > countOfComponentsInCurrentConfig && max > countOfComponentsInCurrentConfig) RequireComponent()
      else if (min <= countOfComponentsInCurrentConfig && max == countOfComponentsInCurrentConfig) RequireNextStep()
      else if (min <= countOfComponentsInCurrentConfig && max > countOfComponentsInCurrentConfig) AllowNextComponent()
  //    else if (max < countOfComponents) ExcludeComponent()
      else ExcludeComponent()
    }
    
    
//    selectionCriterium match {
//      case requireComponent if min > countOfComponents && max > countOfComponents => RequireComponent()
//      case requireNextStep if min <= countOfComponents && max == countOfComponents => RequireNextStep()
//      case allowNextComponent if min <= countOfComponents && max > countOfComponents => AllowNextComponent()
//      case excludeComponent if max < countOfComponents => ExcludeComponent()
//    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return 
   */
  
  private def checkSelectedComponent(
      statusExcludeDependency: StatusExcludeDependency,
      currentStep: StepCurrentConfigBO, 
      componentInId: String): StatusSelectedComponent = {
    val componentIdExist: Boolean = currentStep.components.exists(_.componentId == componentInId)
    statusExcludeDependency match {
      case NotExcludedComponent() =>
        componentIdExist match {
          case true =>
            CurrentConfig.removeComponent(currentStep.stepId, componentInId)
            RemovedComponent()
          case false => AddedComponent()
        }
      case ExcludedComponent() => NotAllowedComponent()
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return ComponentOut
   */
  private def defineStatusSelectedComponent(
      status: StatusComponent,
      statusSelectionCriterium: StatusSelectionCriterium,
      currentStep: Option[StepCurrentConfigBO],
      componentId: String,
      componentNameToShow: String,
      fatherStepId: String,
      dependencies: List[DependencyBO]): ComponentOut = {
    status.selectedComponent.get match {
      case statusErrorComponent: ErrorSelectedComponent => {
        ComponentOut(
            "",
            "",
            status,
            List()
        )
      }
      case statusRemoveComponent: RemovedComponent => {
        ComponentOut(
            componentId,
            fatherStepId,
            status,
            dependencies
         )
      }
      case statusAddComponent: AddedComponent => {
        val component: ComponentBO = ComponentBO(
            componentId,
            componentNameToShow,
            List.empty,
            List.empty,
            List.empty,
            List.empty
        )
        CurrentConfig.addComponent(currentStep.get, component)
//        CurrentConfig.printCurrentConfig
        ComponentOut(
            componentId,
            fatherStepId,
            status,
            dependencies
        )
      }
    }
  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return ComponentOut
   */
  private def defineStatusForSelectionCreterium(
      status: StatusComponent, 
      currentStep: Option[StepCurrentConfigBO],
      componentId: String,
      componentNameToShow: String,
      fatherStepId: String,
      dependencies: List[DependencyBO]): ComponentOut = {
    status.selectionCriterium.get match {
      case statusRequireComponent:   RequireComponent => {
        
        defineStatusSelectedComponent(status, statusRequireComponent, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
      }
      case statusRequireNextStep:    RequireNextStep => {
        
        defineStatusSelectedComponent(status, statusRequireNextStep, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
        
      }
      case statusAllowNextComponent: AllowNextComponent => {
        defineStatusSelectedComponent(status, statusAllowNextComponent, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
      }
      case statusExcludeComponent:   ExcludeComponent => {
        CurrentConfig.printCurrentConfig
        ComponentOut(
            componentId,
            fatherStepId,
            status,
            dependencies
         )
      }
    }
  }
}