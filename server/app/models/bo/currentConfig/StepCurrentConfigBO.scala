package models.bo.currentConfig

import models.bo.component.ComponentBO

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 18.11.2017
 */
case class StepCurrentConfigBO (
                                 stepId: String,
                                 nameToShow: String,
                                 var components: List[ComponentBO] = List(),
                                 var nextStep: Option[StepCurrentConfigBO] = None
)
