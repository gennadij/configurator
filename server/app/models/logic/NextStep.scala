package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.step._
import org.shared.common.status.{Error, Success}

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 01.03.2018
  */
object NextStep {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return NextStepOut
    */
  def getNextStep: NextStepBO = {
    new NextStep().getNextStep
  }
}


class NextStep {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return NextStepOut
    */
  private def getNextStep: NextStepBO = {

    val lastStep: StepCurrentConfigBO = CurrentConfig.getLastStep

    val selectedComponents: List[ComponentBO] = lastStep.components

    selectedComponents match {
      case List() =>
        createErrorNextStepOut(StatusStep(None, Some(StepCurrentConfigBOIncludeNoSelectedComponents()),
          None, Some(Error())))
      case _ =>
        val nextStep: StepBO = Persistence.getNextStep(selectedComponents.head.componentId.get)
        nextStep.status.get.nextStep match {
          case Some(NextStepExist()) =>
            val componentsForSelectionBO: ComponentsForSelectionBO = Persistence.getComponents(nextStep.stepId.get)
            componentsForSelectionBO.status.get.common match {
              case Some(Success()) =>
                lastStep.nextStep = Some(StepCurrentConfigBO(
                  nextStep.stepId.get,
                  nextStep.nameToShow.get,
                  List(),
                  None
                ))
                NextStepBO(
                  step = Some(nextStep),
                  componentsForSelection = Some(componentsForSelectionBO)
                )
              case _ =>
                createErrorNextStepOut(StatusStep(None,
                  Some(NextStepIncludeNoComponents()),
                  None, Some(Error())))
            }
          case _ => NextStepBO(step = Some(nextStep))
        }
    }
  }

  private def createErrorNextStepOut(s: StatusStep): NextStepBO = {
    NextStepBO(step = Some(StepBO(status = Some(s))))
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param list : Seq[String]
    * @return Boolean
    */
  private def compareOneWithAll(list: Seq[String]): Boolean = {
    list match {
      case firstVertex :: rest => rest forall (_ == firstVertex)
    }
  }

}