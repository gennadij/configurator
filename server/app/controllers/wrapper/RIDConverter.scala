package controllers.wrapper

import models.bo._
import org.shared.common.status.Success
import org.shared.component.json.JsonComponentIn

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 14.09.2018
  */
trait RIDConverter extends RidToHash {

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param startConfigBO: StartConfigBO
    * @return SelectedComponentBO
    */
  private[wrapper] def convertRidToHashforStartConfig(startConfigBO: StartConfigBO): StartConfigBO = {
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

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param jsonComponentIn: JsonComponentIn
    * @return SelectedComponentBO
    */
  private[wrapper] def convertHashIdToRidForSelectedComponentBO(jsonComponentIn: JsonComponentIn): SelectedComponentBO = {

    val componentRid: String = getRId(jsonComponentIn.params.componentId) match {
      case Some(id) => id
      case None => ""
    }

    SelectedComponentBO(
      component = Some(ComponentBO(
        componentId = Some(componentRid)
      ))
    )
  }

  /**
    * @author Gennadi Heimann
    * @version 0.0.3
    * @param selectedComponentBO : SelectedComponentBO
    * @return SelectedComponentBO
    */
  private[wrapper] def convertRidToHashForSelectedComponentBO(selectedComponentBO: SelectedComponentBO): SelectedComponentBO = {

    val stepIdHash: Option[String] = selectedComponentBO.currentStep.get.stepId match {
      //TODO SelectedComponent convert
      case Some(sId) => getHash(sId)
      case None => None
    }

    val selectedComponentIdHash: Option[String] = selectedComponentBO.component.get.componentId match {
      case Some(cId) => getHash(cId)
      case None => None
    }

    selectedComponentBO.copy(
      component = Some(selectedComponentBO.component.get.copy(componentId = selectedComponentIdHash)),
      currentStep = Some(selectedComponentBO.currentStep.get.copy(
        stepId = stepIdHash)))

    //        selectedComponentBO.currentStep.get.componentIds match {
    //          case Some(cId) =>
    //            val cIdsHash = cId.map (id => {getHash(id).get})
    //            selectedComponentBO.copy(
    //              currentStep = Some(selectedComponentBO.currentStep.get.copy(
    //                stepId = getHash(sId), componentIds = Some(cIdsHash))))
    //          case None => selectedComponentBO.copy(
    //            currentStep = Some(selectedComponentBO.currentStep.get.copy(
    //              stepId = None, componentIds = Some(List()))))
    //        }
  }
}
