package models.logic

import models.bo.{ContainerComponentBO, StepBO, StepCurrentConfigBO}
import models.currentConfig.CurrentConfig
import org.shared.common.status.{Error, Success}
import org.shared.component.status
import org.shared.component.status._

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 14.02.2018
 */

object SelectedComponentUtil {
  
  def checkStatusSelectedComponent(currentStep: Option[StepCurrentConfigBO],
                                   selectedComponent: ContainerComponentBO, fatherstep: StepBO): ContainerComponentBO = {
    new SelectedComponentUtil().checkStatusSelectedComponent(currentStep, selectedComponent, fatherstep)
  }
}

class SelectedComponentUtil {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return 
   */
  private def checkStatusSelectedComponent(currentStep: Option[StepCurrentConfigBO],
                                           selectedComponent: ContainerComponentBO, fatherStep: StepBO): ContainerComponentBO = {
    
//    val selectedcomponentIdsFromCurrentConfig: List[String] = currentStep match {
//      case Some(step) => step.components map (_.componentId)
//      case None => List()
//    }
//
//    val excludeComponentsIds : List[String] =
//      selectedcomponentIdsFromCurrentConfig flatMap {
//        sCId => (selectedComponent.excludeDependenciesIn map (_.outId)).filter{inECId => sCId == inECId}
//      }
    
//    Logger.info(this.getClass.getSimpleName + ": excludeComponentsIds " + excludeComponentsIds)
    
//    excludeComponentsIds.size match {
//      case count if count > 0 => {
//        //STATUS EXCLUDED_COMPONENT
//        caseExcludedComponent(selectedComponent, currentStep, fatherStep)
//      }
//      case count if count == 0 => {
//        //STATUS NOT_EXCLUDED_COMPONENT
//
//        caseNotExcludedComponent(currentStep, selectedComponent, fatherStep)
//      }
//    }
    ???
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
  private def caseExcludedComponent(selectedComponent: ContainerComponentBO, currentStep: Option[StepCurrentConfigBO],
                                    fatherStep: StepBO): ContainerComponentBO = {
    
//    val
//  }previousSelectedComponentsInCurrentConfig: List[ContainerComponentBO] = currentStep match {
    //      case Some(step) => step.components
    //      case None => List()
    //    }
    //
    //    val currentConfigWithTempSelectedComponent: List[ContainerComponentBO] = previousSelectedComponentsInCurrentConfig :+ selectedComponent
    //
    //    val possibleComponentToSelect: List[String] = getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
    //        fatherStep, selectedComponent, previousSelectedComponentsInCurrentConfig)
    //
    //    possibleComponentToSelect match {
    //      case List() => {
    //        selectedComponent.copy(status = StatusComponent(
    //            Some(RequireNextStep()),
    //            Some(NotAllowedComponent()),
    //            Some(ExcludedComponent()),
    //            Some(Success()),
    //            Some(DefaultComponent())
    //        ))
    //      }
    //      case _ => {
    //        selectedComponent.copy(status = status.StatusComponent(
    //            Some(getSelectionCriteriumStatus(previousSelectedComponentsInCurrentConfig.size, fatherStep)),
    //            Some(NotAllowedComponent()),
    //            Some(ExcludedComponent()),
    //            Some(Success()),
    //            Some(DefaultComponent())
    //        ))
    //      }
    ???
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
  private def caseNotExcludedComponent(currentStep: Option[StepCurrentConfigBO], selectedComponent: ContainerComponentBO,
                                       fatherStep: StepBO): ContainerComponentBO = {
    
//    val componentIdExist: Boolean = currentStep.get.components.exists(_.componentId == selectedComponent.componentId)
//
//    val previousSelectedComponentsInCurrentConfig: List[ContainerComponentBO] = currentStep match {
//      case Some(step) => step.components
//      case None => List()
//    }
//
//    val currentConfigWithTempSelectedComponent: List[ContainerComponentBO] = previousSelectedComponentsInCurrentConfig :+ selectedComponent
//
//    val possibleComponentToSelect: List[String] = getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent,
//        fatherStep, selectedComponent, previousSelectedComponentsInCurrentConfig)
//
//        println("possibleComponentToSelect  "   + possibleComponentToSelect)
//
//    val countOfComponentsInCurrentConfigWithTempSelectedComponent = currentConfigWithTempSelectedComponent.size
//
//    componentIdExist match {
//      case true =>
//        //STATUS REMOVED_COMPONENT
//        CurrentConfig.removeComponent(currentStep.get.stepId, selectedComponent.componentId)
//
//        val countOfComponentsInCurrentConfig: Int = currentStep match {
//          case Some(step) => step.components.size
//          case None => 0
//        }
//
//        selectedComponent.copy(status = status.StatusComponent(
//            Some(getSelectionCriteriumStatus(countOfComponentsInCurrentConfig, fatherStep)),
//            Some(RemovedComponent()),
//            Some(NotExcludedComponent()),
//            Some(Success()),
//            Some(DefaultComponent())
//        ))
//      case false => {
//        //STATUS ADDED_COMPONENT
//        getSelectionCriteriumStatus(countOfComponentsInCurrentConfigWithTempSelectedComponent, fatherStep) match {
//          case RequireComponent() => getComponentBOByStatusRequireComponent(possibleComponentToSelect,
//              selectedComponent, currentStep)
//          case RequireNextStep() => getComponentBOByStatusRequireNextStep(selectedComponent, currentStep)
//          case AllowNextComponent() => getComponentBOByStatusAllowNextComponent(possibleComponentToSelect,
//              selectedComponent, currentStep)
//          case NotAllowNextComponent() => {
//            selectedComponent.copy(status = StatusComponent(
//                Some(RequireNextStep()),
//                Some(NotAllowedComponent()),
//                Some(NotExcludedComponent()),
//                Some(Success()),
//                Some(DefaultComponent())
//            ))
//          }
//          case ErrorSelectionCriterium() => {
//            selectedComponent.copy(status = StatusComponent(
//                Some(ErrorSelectionCriterium()),
//                Some(NotAllowedComponent()),
//                Some(NotExcludedComponent()),
//                Some(Error()),
//                Some(DefaultComponent())
//            ))
//          }
//        }
//      }
//    }
    ???
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
  private def getComponentBOByStatusRequireComponent(possibleComponentToSelect: List[String],
                                                     selectedComponent: ContainerComponentBO, currentStep: Option[StepCurrentConfigBO]) : ContainerComponentBO = {
//    possibleComponentToSelect match {
//      case List() => {
//          val component = selectedComponent.copy(status = StatusComponent(
//              Some(RequireNextStep()),
//              Some(AddedComponent()),
//              Some(NotExcludedComponent()),
//              Some(Success()),
//              Some(DefaultComponent())
//          ))
//
//          CurrentConfig.addComponent(currentStep.get, component)
//
//          component
//      }
//      case _ => {
//        val component = selectedComponent.copy(status = StatusComponent(
//            Some(RequireComponent()),
//            Some(AddedComponent()),
//            Some(NotExcludedComponent()),
//            Some(Success()),
//            Some(DefaultComponent())
//        ))
//
//        CurrentConfig.addComponent(currentStep.get, component)
//
//        component
//      }
//    }
    ???
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
  private def getComponentBOByStatusRequireNextStep(selectedComponent: ContainerComponentBO,
                                                    currentStep: Option[StepCurrentConfigBO]) : ContainerComponentBO = {
//    val component = selectedComponent.copy(status = StatusComponent(
//        Some(RequireNextStep()),
//        Some(AddedComponent()),
//        Some(NotExcludedComponent()),
//        Some(Success()),
//        Some(DefaultComponent())
//    ))
//
//    CurrentConfig.addComponent(currentStep.get, component)
//
//    component
    ???
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
  private def getComponentBOByStatusAllowNextComponent(possibleComponentToSelect: List[String],
                                                       selectedComponent: ContainerComponentBO, currentStep: Option[StepCurrentConfigBO]) : ContainerComponentBO = {
//    possibleComponentToSelect match {
//      case List() => {
//        val component = selectedComponent.copy(status = StatusComponent(
//            Some(RequireNextStep()),
//            Some(AddedComponent()),
//            Some(NotExcludedComponent()),
//            Some(Success()),
//            Some(DefaultComponent())
//        ))
//
//        CurrentConfig.addComponent(currentStep.get, component)
//
//        component
//      }
//      case _ => {
//        val component = selectedComponent.copy(status = StatusComponent(
//            Some(AllowNextComponent()),
//            Some(AddedComponent()),
//            Some(NotExcludedComponent()),
//            Some(Success()),
//            Some(DefaultComponent())
//        ))
//
//        CurrentConfig.addComponent(currentStep.get, component)
//
//        component
//      }
//    }
    ???
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
  private def getFurtherPossibleComponentToSelect(currentConfigWithTempSelectedComponent: List[ContainerComponentBO],
                                                  fatherStep: StepBO, selectedComponent: ContainerComponentBO, previousSelectedComponentsInCurrentConfig: List[ContainerComponentBO]): List[String] = {
    
//    val outExcludedDependencyIds: Set[String] = (currentConfigWithTempSelectedComponent flatMap (pSC => {
//      pSC.excludeDependenciesOut map (_.inId)
//    })).toSet
//
//    val unselectedComponentsIds: List[String] = fatherStep.componentIds filterNot (c => {
//      currentConfigWithTempSelectedComponent.map(_.componentId).contains(c)
//    })
//
//    val unselectedExcludedComponents: List[String] = unselectedComponentsIds.filter(uC => {
//      outExcludedDependencyIds.contains(uC)
//    })
//
//    val filteredComponents = fatherStep.componentIds.filterNot(c => (unselectedExcludedComponents :+ selectedComponent.componentId).contains(c))
//
//    filteredComponents.filterNot(pCTS => previousSelectedComponentsInCurrentConfig.map(_.componentId).contains(pCTS))
    ???
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
  private def getSelectionCriteriumStatus(countOfComponents: Int, fatherStep: StepBO): StatusSelectionCriterium = {
    
//    val min: Int = fatherStep.selectionCriteriumMin
//    val max: Int = fatherStep.selectionCriteriumMax
//
//    countOfComponents match {
//      case count if min > count && max > count =>    RequireComponent()
//      case count if min <= count && max == count =>  RequireNextStep()
//      case count if min <= count && max > count =>   AllowNextComponent()
//      case count if max < count =>                   NotAllowNextComponent()
//      case _ =>                                      ErrorSelectionCriterium()
//    }
    ???
  }
}