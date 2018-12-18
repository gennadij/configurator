package models.configLogic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.status.common.{StatusStep, Success}
import org.shared.status.currentConfig.StepCurrentConfigBOIncludeNoSelectedComponents
import org.shared.status.nextStep.{NextStepExist, NextStepIncludeNoComponents}

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
  def getNextStep(currentConfig: CurrentConfig): NextStepBO = {
    new NextStep(currentConfig).getNextStep
  }
}


class NextStep(currentConfig: CurrentConfig) {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return NextStepOut
    */
  private def getNextStep: NextStepBO = {

    val lastStep: StepCurrentConfigBO = currentConfig.getLastStep

    val selectedComponents: List[ComponentBO] = lastStep.components

    selectedComponents match {
      case List() =>
        NextStepBO(
          step = Some(StepBO(
            status = Some(StatusStep(
              currentConfig = Some(StepCurrentConfigBOIncludeNoSelectedComponents()),
              common = Some(Success())
            ))
          ))
        )
      case _ =>
        val nextStep: StepBO = Persistence.getNextStep(selectedComponents.head.componentId.get)
        nextStep.status.get.nextStep match {
          case Some(NextStepExist()) =>
            val componentsForSelectionBO: ComponentsForSelectionBO = Persistence.getComponents(nextStep.stepId.get)
            componentsForSelectionBO.status.get.common match {
              case Some(Success()) =>
                //Step zu der CurrentConfig hinzufuegen
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
              case _ => NextStepBO(step = Some(StepBO(
                  status = Some(StatusStep(
                    nextStep = Some(NextStepIncludeNoComponents()),
                    common = Some(Success())
                  ))
              )))
            }
          case _ => NextStepBO(step = Some(nextStep))
        }
    }
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.1
    * @param list : Seq[String]
    * @return Boolean
    */
//  private def compareOneWithAll(list: Seq[String]): Boolean = {
//    list match {
//      case firstVertex :: rest => rest forall (_ == firstVertex)
//    }
//  }

}