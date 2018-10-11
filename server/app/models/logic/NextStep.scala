package models.logic

import models.bo._
import models.currentConfig.CurrentConfig
import models.persistence.Persistence
import org.shared.common.status.Success
import org.shared.common.status.step._

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
                //TODO NextStep erst zu der CurrentConfig hinzufuegen wenn erste Component ausgewaehlt wurde.
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