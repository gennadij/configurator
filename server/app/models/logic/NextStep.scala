package models.logic

import models.bo.{ComponentBO, ContainerComponentBO, StepBO, StepCurrentConfigBO}
import models.currentConfig.CurrentConfig
import models.persistence.orientdb.Graph
import models.wrapper.nextStep.NextStepOut
import org.shared.common.status.step._
import org.shared.common.status.{Error, Success}
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
   * @return NextStepOut
   */
  def getNextStep: NextStepOut = {
    new NextStep().getNextStep
  }
}


class NextStep {
  
  /**
   * @author Gennadi Heimann
   * 
   * @version 0.0.2
   * 
   * @return NextStepOut
   */
  private def getNextStep: NextStepOut = {
    
    val lastStep: StepCurrentConfigBO = CurrentConfig.getLastStep
    
    val selectedComponents: List[ComponentBO] = ???
      //lastStep.components.get
    Logger.info(selectedComponents.toString())
    selectedComponents match {
      case List() => {
        createErrorNextStepOut(StatusStep(None, Some(StepCurrentConfigBOIncludeNoSelectedComponents()),
            None, Some(Error())))
      }
      case _ => {
        val nextStep: StepBO = Graph.getNextStep(selectedComponents.head.componentId.get)
        nextStep.status.nextStep match {
          case Some(NextStepExist()) => {
            val components: List[ContainerComponentBO] = ???
//              Graph.getComponents(nextStep.stepId.get)
            val statusComponents: Boolean = ???
//              components map { _.status.common.get } contains{ Success() }
            statusComponents match {
              case true => {
              lastStep.nextStep = Some(StepCurrentConfigBO(
                    nextStep.stepId.get,
                    nextStep.nameToShow.get,
                None,
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
            componentIds = None
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