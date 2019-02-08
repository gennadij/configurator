package models.bo.component

import models.bo.currentConfig.StepCurrentConfigBO
import models.bo.info.InfoBO
import models.bo.step.StepContainerBO
import models.bo.warning.WarningBO
import org.shared.error.Error

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.02.2018
  */
case class SelectedComponentContainerBO(
                                         selectedComponent: Option[SelectedComponentBO] = None,
                                         currentStep: Option[StepContainerBO] = None,
                                         nextStep: Option[StepContainerBO] = None,
                                         stepCurrentConfig: Option[StepCurrentConfigBO] = None,
                                         possibleComponentIdsToSelect : Option[List[String]] = None,
                                         addedComponentToCurrentConfig: Option[Boolean] = None,
                                         info: Option[InfoBO] = None,
                                         errors: Option[List[Error]] = None,
                                         warning: Option[WarningBO] = None
                      )
