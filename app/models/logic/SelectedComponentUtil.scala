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
import models.status.component.ErrorSelectionCriterium
import models.status.component.StatusSelectionCriterium

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
      previousSelectedComponents: List[ComponentBO], 
      fatherStep: StepBO,
      statusSelectedComponent: StatusSelectedComponent,
      selectedComponentBO: ComponentBO): StatusSelectionCriterium = {
    new SelectedComponentUtil().checkSelectionCriterium(
        previousSelectedComponents, 
        fatherStep, statusSelectedComponent, selectedComponentBO)
  }
  
  def checkSelectedComponent(
      statusExcludeDependency: StatusExcludeDependency,
      currentStep: StepCurrentConfigBO, 
      selectedComponentBO: ComponentBO): StatusSelectedComponent = {
    new SelectedComponentUtil().checkSelectedComponent(statusExcludeDependency, currentStep, selectedComponentBO)
  }
  
//  def defineStatusForSelectionCreterium(
//      status: StatusComponent, 
//      currentStep: Option[StepCurrentConfigBO],
//      selectedComponentBO: ComponentBO,
//      fatherStepId: String): ComponentOut = {
//    new SelectedComponentUtil().defineStatusForSelectionCreterium(
//        status, currentStep, selectedComponentBO, fatherStepId)
//  }
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
      previousSelectedComponents: List[ComponentBO], 
      fatherStep: StepBO,
      statusSelectedComponent: StatusSelectedComponent,
      selectedComponentBO: ComponentBO): StatusSelectionCriterium = {
    
    val outExcludedDependencyIds: List[String] = previousSelectedComponents flatMap (pSC => {
      pSC.excludeDependenciesOut map (_.inId)
    })
    
    val outExcludedDependencyIdsWithOutDupplicate = outExcludedDependencyIds.toSet
    
    val unselectedComponentsIds: List[String] = fatherStep.componentIds filterNot (c => {
      previousSelectedComponents.map(_.componentId).contains(c)
    })

    val unselectedExcludedComponents: List[String] = unselectedComponentsIds.filterNot(uC => {
      outExcludedDependencyIdsWithOutDupplicate.contains(uC)
    })
    
//    Logger.info(this.getClass.getSimpleName + " countOfComponents : " + countOfComponents)
//    Logger.info(this.getClass.getSimpleName + " selectionCriterium : " + min + " " + max)
    
    val countOfComponentsInCurrentConfig = previousSelectedComponents.size
    
    statusSelectedComponent match {
      case AddedComponent() => {
        unselectedExcludedComponents match {
          case List() => RequireNextStep()
          case _ => setSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)
        }
      }
      case RemovedComponent() => setSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)
      case ErrorSelectedComponent() => setSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)
      case NotAllowedComponent() => {
        unselectedExcludedComponents match {
          case List() => RequireNextStep()
          case _ => setSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)
        }
      }
    }
  }
  
  private def setSelectionCriteriumStatus(countOfComponents: Int, fatherStep: StepBO): StatusSelectionCriterium = {
    
    val min: Int = fatherStep.selectionCriteriumMin
    val max: Int = fatherStep.selectionCriteriumMax
    
    countOfComponents match {
      case count if min > count && max > count =>    RequireComponent()
      case count if min <= count && max == count =>  RequireNextStep()
      case count if min <= count && max > count =>   AllowNextComponent()
      case count if max < count =>                   ExcludeComponent()
      case _ =>                                      ErrorSelectionCriterium()
    }
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
      selectedComponentBO: ComponentBO): StatusSelectedComponent = {
    val componentIdExist: Boolean = currentStep.components.exists(_.componentId == selectedComponentBO.componentId)
    statusExcludeDependency match {
      case NotExcludedComponent() =>
        componentIdExist match {
          case true =>
            CurrentConfig.removeComponent(currentStep.stepId, selectedComponentBO.componentId)
            RemovedComponent()
          case false => {

            CurrentConfig.addComponent(currentStep, selectedComponentBO)
            CurrentConfig.printCurrentConfig
            AddedComponent()
          }
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
//  private def defineStatusSelectedComponent(
//      status: StatusComponent,
//      statusSelectionCriterium: StatusSelectionCriterium,
//      currentStep: Option[StepCurrentConfigBO],
//      selectedComponentBO: ComponentBO,
//      fatherStepId: String): ComponentOut = {
//    status.selectedComponent.get match {
//      case statusErrorComponent: ErrorSelectedComponent => {
//        ComponentOut(
//            "",
//            "",
//            status,
//            List()
//        )
//      }
//      case statusRemoveComponent: RemovedComponent => {
//        ComponentOut(
//            selectedComponentBO.componentId,
//            fatherStepId,
//            status,
//            selectedComponentBO.requireDependenciesOut ::: selectedComponentBO.excludeDependenciesOut
//         )
//      }
//      case statusAddComponent: AddedComponent => {
//        val component: ComponentBO = ComponentBO(
//            selectedComponentBO.componentId,
//            selectedComponentBO.nameToShow,
//            selectedComponentBO.excludeDependenciesOut,
//            selectedComponentBO.excludeDependenciesIn,
//            selectedComponentBO.requireDependenciesOut,
//            selectedComponentBO.requireDependenciesIn
//        )
//        CurrentConfig.addComponent(currentStep.get, component)
////        CurrentConfig.printCurrentConfig
//        ComponentOut(
//            selectedComponentBO.componentId,
//            fatherStepId,
//            status,
//            selectedComponentBO.requireDependenciesOut ::: selectedComponentBO.excludeDependenciesOut
//        )
//      }
//    }
//  }
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return ComponentOut
   */
//  private def defineStatusForSelectionCreterium(
//      status: StatusComponent, 
//      currentStep: Option[StepCurrentConfigBO],
//      selectedComponentBO: ComponentBO,
//      fatherStepId: String): ComponentOut = {
//    status.selectionCriterium.get match {
//      case statusRequireComponent:   RequireComponent => {
//        
//        defineStatusSelectedComponent(status, statusRequireComponent, currentStep, 
//            selectedComponentBO, fatherStepId)
//      }
//      case statusRequireNextStep:    RequireNextStep => {
//        
//        defineStatusSelectedComponent(status, statusRequireNextStep, currentStep, 
//            selectedComponentBO, fatherStepId)
//        
//      }
//      case statusAllowNextComponent: AllowNextComponent => {
//        defineStatusSelectedComponent(status, statusAllowNextComponent, currentStep, 
//            selectedComponentBO, fatherStepId)
//      }
//      case statusExcludeComponent:   ExcludeComponent => {
//        CurrentConfig.printCurrentConfig
//        ComponentOut(
//            selectedComponentBO.componentId,
//            fatherStepId,
//            status,
//            selectedComponentBO.requireDependenciesOut ::: selectedComponentBO.excludeDependenciesOut
//         )
//      }
//    }
//  }
}