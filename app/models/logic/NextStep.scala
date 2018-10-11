package models.logic

import models.bo.StepCurrentConfigBO
import models.currentConfig.CurrentConfig
import models.bo.ComponentBO
import models.bo.StepBO
import models.status.step.MultipleNextSteps
import models.status.step.NextStepNotExist
import models.status.step.CommonErrorNextStep
import models.status.step.NextStepExist
import models.wrapper.nextStep.NextStepOut
import models.persistence.orientdb.Graph
import models.status.step.StatusStep
import models.status.Error
import models.status.step.StepCurrentConfigBOIncludeNoSelectedComponents
import models.status.step.NextStepIncludeNoComponents
import models.status.Status
import models.status.Success
import play.api.Logger

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 01.03.2018
 */
object NextStep{
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return NextStepOut
   */
  def getNextStep(): NextStepOut = {
    new NextStep().getNextStep()
  }
}


class NextStep {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @param 
   * 
   * @return NextStepOut
   */
  private def getNextStep(): NextStepOut = {
    
    val lastStep: StepCurrentConfigBO = CurrentConfig.getLastStep
    
    val selectedComponents: List[ComponentBO] = lastStep.components
    
    Logger.info(selectedComponents.toString())
    selectedComponents match {
      case List() => {
        createErrorNextStepOut(StatusStep(None, Some(StepCurrentConfigBOIncludeNoSelectedComponents()),
            None, Some(Error())))
      }
      case _ => {
        val nextStep: StepBO = Graph.getNextStep(selectedComponents.head.componentId)
        nextStep.status.nextStep match {
          case Some(NextStepExist()) => {
            val components: List[ComponentBO] = Graph.getComponents(nextStep.stepId)
            val statusComponents: Boolean = components map { _.status.common.get } contains{ Success() }
            statusComponents match {
              case true => {
              lastStep.nextStep = Some(StepCurrentConfigBO(
                    nextStep.stepId,
                    nextStep.nameToShow,
                    List(),
                    None
                ))
                NextStepOut(
                   nextStep,
                   components
                )
              }
              case false => {
                createErrorNextStepOut(StatusStep(None, 
                  Some(NextStepIncludeNoComponents()),
                  None, Some(Error())))
              }
            }
          }
          case _ => NextStepOut(nextStep, List())
        }
      }
    }
  }
  
  private def createErrorNextStepOut(s: StatusStep): NextStepOut = {
    NextStepOut(
        StepBO(
            status = s,
            componentIds = List()
        ),
        List()
    )
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
    private def compareOneWithAll(list: Seq[String]): Boolean = {
      list match {
        case firstVertex :: rest => rest forall (_ == firstVertex)
      }
    }
  
}