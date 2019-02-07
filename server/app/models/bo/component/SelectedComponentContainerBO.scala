package models.bo.component

import models.bo.info.InfoBO
import models.bo.step.{StepContainerBO, StepCurrentConfigBO}
import models.bo.warning.WarningBO
import org.shared.error.Error

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.02.2018
  */
case class SelectedComponentContainerBO(
                                         //                                status: Option[StatusComponent] = None,
                                         selectedComponent: Option[ComponentBO] = None,
                                         currentStep: Option[StepContainerBO] = None,
                                         nextStep: Option[StepContainerBO] = None,
                                         stepCurrentConfig: Option[StepCurrentConfigBO] = None,
                                         possibleComponentIdsToSelect : Option[List[String]] = None,
                                         info: Option[InfoBO] = None,
                                         errors: Option[List[Error]] = None,
                                         warning: Option[WarningBO] = None
                      )
