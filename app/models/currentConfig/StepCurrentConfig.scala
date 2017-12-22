package models.currentConfig

import models.wrapper.common.Component

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 18.11.2017
 */
case class StepCurrentConfig (
    stepId: String,
    nameToShow: String,
    var components: List[Component],
    var nextStep: Option[StepCurrentConfig]
)