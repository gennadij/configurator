package models.configLogic

import models.bo.component.ComponentBO
import models.bo.{step, _}
import models.bo.step.{StepContainerBO, StepCurrentConfigBO}
import models.persistence.Persistence
import org.shared.error.{Error, PreviousStepIncludeNoSelectedComponents}

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
  def getNextStep(currentConfig: CurrentConfig): StepContainerBO = {
    new NextStep(currentConfig).getNextStep
  }
}


class NextStep(currentConfig: CurrentConfig) {

  /**
    * @author Gennadi Heimann
    * @version 0.0.2
    * @return NextStepOut
    */
  private def getNextStep: StepContainerBO = {

    val lastStep: StepCurrentConfigBO = currentConfig.getLastStep

    val selectedComponents: List[ComponentBO] = lastStep.components

    selectedComponents match {
      case List() =>
        step.StepContainerBO(
          error = Some(List(PreviousStepIncludeNoSelectedComponents(lastStep.stepId)))
        )
      case _ =>
        val nextStep: StepContainerBO = Persistence.getStep(componentId = selectedComponents.head.componentId)
        nextStep.error match {
          case Some(e) => StepContainerBO(error = Some(e))
          case None =>
            val (componentsBO, errorComponent): (Option[Set[ComponentBO]], Option[Error])  =
              Persistence.getComponents(nextStep.step.get.stepId.get)
            errorComponent match {
              case Some(_) => step.StepContainerBO(error = Some(List(errorComponent.get)))
              case None =>
                //Step zu der CurrentConfig hinzufuegen
                lastStep.nextStep = Some(StepCurrentConfigBO(
                  stepId = nextStep.step.get.stepId.get,
                  nameToShow = nextStep.step.get.nameToShow.get
                ))
                step.StepContainerBO(
                  step = nextStep.step,
                  componentsForSelection = componentsBO
                )
            }
        }
    }
  }

//  /**
//    * @author Gennadi Heimann
//    * @version 0.0.1
//    * @param list : Seq[String]
//    * @return Boolean
//    */
//  private def compareOneWithAll(list: Seq[String]): Boolean = {
//    list match {
//      case firstVertex :: rest => rest forall (_ == firstVertex)
//    }
//  }

}