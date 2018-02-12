package models.logic

import models.wrapper.component.ComponentIn
import models.wrapper.component.ComponentOut
import models.bo.ComponentBO
import models.persistence.Persistence
import models.bo.StepBO
import models.currentConfig.CurrentConfig
import models.bo.StepCurrentConfigBO
import models.status.component.StatusSelectedComponent
import models.status.component.StatusSelectionCriterium
import models.status.component.StatusExcludeDependency
import models.status.component.AddedComponent
import models.status.component.RemovedComponent
import models.status.component.ErrorComponent
import models.status.component.RequireComponent
import models.status.component.RequireNextStep
import models.status.component.AllowNextComponent
import models.status.component.ExcludeComponent
import models.bo.DependencyBO
import models.status.component.ExcludedComponent
import models.status.component.NotExcludedComponent
import models.status.Status
import models.status.Success
import models.status.component.StatusComponent
import models.status.Error
import models.status.FinalComponent

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 08.02.2018
 */
class SelectedComponent {
  
  def verifySelectedComponent(componentIn: ComponentIn): ComponentOut = {
    
    
    val selectedComponentBO: Option[ComponentBO] = Persistence.getSelectedComponent(componentIn)
    
    val fatherStep: Option[StepBO] = Persistence.getFatherStep(selectedComponentBO.get.componentId)
    
    val currentStep: Option[StepCurrentConfigBO] = CurrentConfig.getCurrentStep(fatherStep.get.stepId)
    
    val nextStep: Option[StepBO] = Persistence.getNextStep(selectedComponentBO.get.componentId)
      
//      Logger.info(this.getClass.getSimpleName + ": currentStep from CurrentConfig " + currentStep.get.getClass.hashCode())

    val statusSelectedComponent: StatusSelectedComponent = checkSelectedComponent(currentStep, componentIn.componentId)
      
      val previousSelectedComponents: List[ComponentBO] = currentStep match {
        case Some(step) => step.components
        case None => List()
      }
     
//      Logger.info(this.getClass.getSimpleName + ": previousSelectedComponents " + previousSelectedComponents)
      
      val stausSelectionCriterium: StatusSelectionCriterium = 
          checkSelectionCriterium(previousSelectedComponents.size, fatherStep.get, statusSelectedComponent)
      
//      Logger.info(this.getClass.getSimpleName + ": " + stausSelectionCriterium)
      
      val statusExcludeDependencies: StatusExcludeDependency = checkExcludeDependencies(
          currentStep.get, 
          selectedComponentBO.get.excludeDependenciesIn)
      
      val nextStepExistence: Boolean = checkNextStepExistence(nextStep)
      
      val commonStatus: Status = Success()
      
      val status: StatusComponent = statusExcludeDependencies match {
        case NotExcludedComponent() => {
          StatusComponent(
            Some(stausSelectionCriterium), 
            Some(statusSelectedComponent), 
            Some(statusExcludeDependencies), 
            Some(commonStatus), 
            Some(nextStepExistence))
        }
        case ExcludedComponent() => {
          StatusComponent(
            Some(stausSelectionCriterium), 
            Some(ErrorComponent()), 
            Some(statusExcludeDependencies), 
            Some(Error()), 
            Some(nextStepExistence))
        }
      }
      
      val dependencies: List[DependencyBO] = 
        selectedComponentBO.get.requireDependenciesOut ::: selectedComponentBO.get.excludeDependenciesOut
    
    createComponentOut(
        status, 
        currentStep, 
        selectedComponentBO.get.componentId, 
        selectedComponentBO.get.nameToShow, 
        fatherStep.get.stepId, 
        dependencies)
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
  def createComponentOut(
      status: StatusComponent, 
      currentStep: Option[StepCurrentConfigBO],
      componentId: String, 
      componentNameToShow: String,
      fatherStepId: String, 
      dependencies: List[DependencyBO]): ComponentOut = {
    
    status.excludeDependency.get match {
      case statusExcludedComponent: ExcludedComponent => {
        ComponentOut(  
            componentId,
            fatherStepId,
            status,
            List()
         )
      }
      case statusNotExcludedComponent: NotExcludedComponent => {
        defineStatusForSelectionCreterium(status, currentStep, componentId, componentNameToShow, fatherStepId, dependencies)
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
  def defineStatusForSelectionCreterium(
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
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return ComponentOut
   */
  def defineStatusSelectedComponent(
      status: StatusComponent,
      statusSelectionCriterium: StatusSelectionCriterium,
      currentStep: Option[StepCurrentConfigBO],
      componentId: String,
      componentNameToShow: String,
      fatherStepId: String,
      dependencies: List[DependencyBO]): ComponentOut = {
    status.selectedComponent.get match {
      case statusErrorComponent: ErrorComponent => {
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
        status.nextStepExistence.get match {
          case true => {
            ComponentOut(
                componentId,
                fatherStepId,
                status,
                dependencies
            )
          }
          case false => {
            val finalStepStatus = status.copy(common = Some(FinalComponent()))
            ComponentOut(
                componentId,
                fatherStepId,
                status, //TODO Final Step Status fehlt
                dependencies
            )
          }
        }
      }
    }
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
  
  def checkSelectedComponent(currentStep: Option[StepCurrentConfigBO], componentInId: String): StatusSelectedComponent = {
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
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return
   */
  
  def checkSelectionCriterium(
      countOfSelectedComponents: Int, 
      fatherStep: StepBO,
      statusSelectedComponent: StatusSelectedComponent): StatusSelectionCriterium = {
    //Ungepruefte Komponente, die noch nicht in der aktuellen Konfiguration hinzugefuegt wird
    
    
    val countOfComponents = statusSelectedComponent match {
      case AddedComponent() => countOfSelectedComponents + 1
      case RemovedComponent() => countOfSelectedComponents
      case ErrorComponent() => countOfSelectedComponents
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
   * Prueft ob die ausgewaelte Komponente schliesst eine von der Komponente in der aktuellen Konfiguration aus
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return
   */
  def checkExcludeDependencies(currentStep: StepCurrentConfigBO, inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
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
   * Prueft ob die ausgewaelte Komponente schliesst eine von der Komponente in der aktuellen Konfiguration aus
   * 
   * @version 0.0.1
   * 
   * @param 
   * 
   * @return
   */
  def checkNextStepExistence(nextStep: Option[StepBO]): Boolean = {
    
    nextStep match {
      case Some(nextStep) => true
      case None => false
    }
    
//    vComponent.getEdges(Direction.OUT, PropertyKeys.HAS_STEP).asScala.toList match {
//      case eHasSteps if eHasSteps.size > 0 => true
//      case eHasSteps if eHasSteps.size == 0 => false
//    }
  }
}