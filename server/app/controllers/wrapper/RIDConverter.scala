package controllers.wrapper

import models.bo.{ComponentsForSelectionBO, StartConfigBO, StepBO}
import org.shared.common.status.Success

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.09.2018
  */
trait RIDConverter extends RidToHash {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO: SelectedComponentBO
    * @return SelectedComponentBO
    */
  def convertRIDforStartConfig(startConfigBO: StartConfigBO): StartConfigBO = {
    startConfigBO.step.get.status.get.common match {
      case Some(Success()) =>
        val stepIdHash: String = setIdAndHash(startConfigBO.step.get.stepId.get)._2

        val stepBOWithHashId: StepBO = startConfigBO.step.get.copy(stepId = Some(stepIdHash))

        val componentsBOWithHashId: ComponentsForSelectionBO =
          startConfigBO.componentsForSelection.get.status.get.common match {
            case Some(Success()) =>
              ComponentsForSelectionBO(
                status = startConfigBO.componentsForSelection.get.status,
                components = startConfigBO.componentsForSelection.get.components map (c => {
                  c.copy(componentId = Some(setIdAndHash(c.componentId.get)._2))
                }))
            case _ => startConfigBO.componentsForSelection.get
          }

        startConfigBO.copy(step = Some(stepBOWithHashId), componentsForSelection = Some(componentsBOWithHashId))

      case _ => startConfigBO
    }

  }
}
