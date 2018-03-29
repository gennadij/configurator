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
import models.status.component.DefaultComponent

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
      currentStep: Option[StepCurrentConfigBO], 
      inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
    new SelectedComponentUtil().checkExcludeDependencies(currentStep, inExcludeDependencies)
  }
  
  def checkSelectionCriterium(
      currentStep: Option[StepCurrentConfigBO],
      fatherStep: StepBO,
      selectedComponentBO: ComponentBO): ComponentBO = {
    new SelectedComponentUtil().checkSelectionCriterium(
        currentStep, fatherStep, selectedComponentBO)
  }
  
  def checkSelectedComponentInCurrentConfig(
      statusExcludeDependency: StatusExcludeDependency,
      currentStep: StepCurrentConfigBO, 
      selectedComponentBO: ComponentBO): StatusSelectedComponent = {
    new SelectedComponentUtil().checkSelectedComponentInCurrentConfig(statusExcludeDependency, currentStep, selectedComponentBO)
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
      currentStep: Option[StepCurrentConfigBO], 
      inExcludeDependencies: List[DependencyBO]): StatusExcludeDependency = {
    val selectedcomponentIds: List[String] = currentStep match {
      case Some(step) => step.components map (_.componentId)
      case None => List()
    }
      
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
      currentStep: Option[StepCurrentConfigBO], 
      fatherStep: StepBO,
      selectedComponentBO: ComponentBO): ComponentBO = {
    
    val statusSelectedComponent: StatusSelectedComponent = selectedComponentBO.status.selectedComponent.get
    
    val previousSelectedComponentsInCurrentConfig: List[ComponentBO] = currentStep match {
        case Some(step) => step.components
        case None => List()
      }
    
    val currentConfigWithTempSelectedComponent: List[ComponentBO] = previousSelectedComponentsInCurrentConfig :+ selectedComponentBO
    
    val outExcludedDependencyIds: List[String] = currentConfigWithTempSelectedComponent flatMap (pSC => {
      pSC.excludeDependenciesOut map (_.inId)
    })
    
    val outExcludedDependencyIdsWithOutDupplicate = outExcludedDependencyIds.toSet
    
    val unselectedComponentsIds: List[String] = fatherStep.componentIds filterNot (c => {
      currentConfigWithTempSelectedComponent.map(_.componentId).contains(c)
    })

    val unselectedExcludedComponents: List[String] = unselectedComponentsIds.filterNot(uC => {
      outExcludedDependencyIdsWithOutDupplicate.contains(uC)
    })
    
//    Logger.info(this.getClass.getSimpleName + " countOfComponents : " + countOfComponents)
//    Logger.info(this.getClass.getSimpleName + " selectionCriterium : " + min + " " + max)
    
    val countOfComponentsInCurrentConfig = currentConfigWithTempSelectedComponent.size
    
    val statusExcludeDependency: StatusExcludeDependency = selectedComponentBO.status.excludeDependency.get
    
    statusExcludeDependency match{
      case ExcludedComponent() => {
        unselectedExcludedComponents match {
          case List() => {
            selectedComponentBO.copy(status = StatusComponent(
                Some(RequireNextStep()),
                Some(NotAllowedComponent()),
                Some(statusExcludeDependency),
                Some(Success()),
                Some(DefaultComponent())
            ))
          }
          case _ => {
            selectedComponentBO.copy(status = StatusComponent(
                Some(setSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size, fatherStep)),
                Some(NotAllowedComponent()),
                Some(statusExcludeDependency),
                Some(Success()),
                Some(DefaultComponent())
            ))
          }
        }
        
      }
      case NotExcludedComponent() => {
        statusSelectedComponent match {
          case AddedComponent() => {
            unselectedExcludedComponents match {
              case List() => {
                val component = selectedComponentBO.copy(status = StatusComponent(
                    Some(RequireNextStep()),
                    Some(statusSelectedComponent),
                    Some(statusExcludeDependency),
                    Some(Success()),
                    Some(DefaultComponent())
                ))
                 
                CurrentConfig.addComponent(currentStep.get, component)
                
                component
              }
              case _ => {
                val component = selectedComponentBO.copy(status = StatusComponent(
                    Some(setSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)),
                    Some(statusSelectedComponent),
                    Some(statusExcludeDependency),
                    Some(Success()),
                    Some(DefaultComponent())
                ))
                
                CurrentConfig.addComponent(currentStep.get, component)
                
                component
              }
            }
          }
          case RemovedComponent() => {
            selectedComponentBO.copy(status = StatusComponent(
                    Some(setSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size, fatherStep)),
                    Some(statusSelectedComponent),
                    Some(statusExcludeDependency),
                    Some(Success()),
                    Some(DefaultComponent())
                ))
          }
          case ErrorSelectedComponent() => {
            selectedComponentBO.copy(status = StatusComponent(
                    Some(setSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size, fatherStep)),
                    Some(statusSelectedComponent),
                    Some(statusExcludeDependency),
                    Some(Success()),
                    Some(DefaultComponent())
                ))
          }
          case NotAllowedComponent() => {
            unselectedExcludedComponents match {
              case List() => {
                selectedComponentBO.copy(status = StatusComponent(
                    Some(RequireNextStep()),
                    Some(statusSelectedComponent),
                    Some(statusExcludeDependency),
                    Some(Success()),
                    Some(DefaultComponent())
                    
                ))
              }
              case _ => {
                selectedComponentBO.copy(status = StatusComponent(
                    Some(setSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)),
                    Some(statusSelectedComponent),
                    Some(statusExcludeDependency),
                    Some(Success()),
                    Some(DefaultComponent())
                ))              
              }
            }
          }
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
  
  private def checkSelectedComponentInCurrentConfig(
      statusExcludeDependency: StatusExcludeDependency,
      currentStep: StepCurrentConfigBO, 
      selectedComponentBO: ComponentBO): StatusSelectedComponent = {
    //TODO v0.0.3 Status Require Dependency => wird in der v0.0.3 implementiert
    val componentIdExist: Boolean = currentStep.components.exists(_.componentId == selectedComponentBO.componentId)
    
    componentIdExist match {
      case true =>
        CurrentConfig.removeComponent(currentStep.stepId, selectedComponentBO.componentId)
        RemovedComponent()
      case false => AddedComponent()
    }
  }
//    statusExcludeDependency match {
//      case NotExcludedComponent() =>
//        componentIdExist match {
//          case true =>
//            CurrentConfig.removeComponent(currentStep.stepId, selectedComponentBO.componentId)
//            RemovedComponent()
//          case false => {
//
//            CurrentConfig.addComponent(currentStep, selectedComponentBO)
//            CurrentConfig.printCurrentConfig
//            AddedComponent()
//          }
//        }
//      case ExcludedComponent() => NotAllowedComponent()
//    }
//  }
}