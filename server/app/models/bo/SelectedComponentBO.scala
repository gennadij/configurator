package models.bo

import org.shared.status.selectedComponent.StatusComponent

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.02.2018
  */
case class SelectedComponentBO(
                                status: Option[StatusComponent] = None,
                                selectedComponent: Option[ComponentBO] = None,
                                currentStep: Option[StepBO] = None,
                                nextStep: Option[StepBO] = None,
                                stepCurrentConfig: Option[StepCurrentConfigBO] = None,
                                possibleComponentIdsToSelect : Option[List[String]] = None
                      )