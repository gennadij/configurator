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
      currentStep: StepCurrentConfigBO, 
      inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
    new SelectedComponentUtil().checkExcludeDependencies(currentStep, inExcludeDependencies)
  }
  
  def checkSelectionCriterium(
      countOfSelectedComponents: Int, 
      fatherStep: StepBO,
      statusSelectedComponent: StatusSelectedComponent): StatusSelectionCriterium = {
    new SelectedComponentUtil().checkSelectionCriterium(
        countOfSelectedComponents, 
        fatherStep, statusSelectedComponent)
  }
  
  def checkSelectedComponent(
      currentStep: Option[StepCurrentConfigBO], 
      componentInId: String): StatusSelectedComponent = {
    new SelectedComponentUtil().checkSelectedComponent(currentStep, componentInId)
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
      currentStep: StepCurrentConfigBO, 
      inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
    val selectedcomponentIds: List[String] = currentStep.components map (_.componentId)
    val inExcludeComponentIds: List[String] = inExcludeDependencies map (_.outId)
    
    val excludeComponentsIds: List[String] = selectedcomponentIds flatMap { sCId => inExcludeComponentIds.filter{inECId => sCId == inECId} }
    
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
      statusSelectedComponent: StatusSelectedComponent): StatusSelectionCriterium = {
    //Ungepruefte Komponente, die noch nicht in der aktuellen Konfiguration hinzugefuegt wird
    
    
    val countOfComponents = statusSelectedComponent match {
      case AddedComponent() => countOfSelectedComponents + 1
      case RemovedComponent() => countOfSelectedComponents
      case ErrorSelectedComponent() => countOfSelectedComponents
    }
    
    val min: Int = fatherStep.selectionCriteriumMin
    val max: Int = fatherStep.selectionCriteriumMax
    
//    Logger.info(this.getClass.getSimpleName + " countOfComponents : " + countOfComponents)
//    Logger.info(this.getClass.getSimpleName + " selectionCriterium : " + min + " " + max)
    
    if(min > countOfComponents && max > countOfComponents) RequireComponent()
    else if (min <= countOfComponents && max == countOfComponents) RequireNextStep()
    else if (min <= countOfComponents && max > countOfComponents) AllowNextComponent()
//    else if (max < countOfComponents) ExcludeComponent()
    else ExcludeComponent()
    
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
   * @param StatusComponent
   * 
   * @return ComponentOut
   */
  
  private def checkSelectedComponent(
      currentStep: Option[StepCurrentConfigBO], 
      componentInId: String): StatusSelectedComponent = {
    currentStep match {
      case Some(step) => {
        step.components.exists(_.componentId == componentInId) match {
          case true => {
            CurrentConfig.removeComponent(currentStep.get.stepId, componentInId)
            RemovedComponent()
          }
          case false => AddedComponent()
        }
      }
      case None => AddedComponent()
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