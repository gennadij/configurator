package org.oldClientScalaJs.wrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 02.01.2018
 */
case class StepCurrentConfig (
    stepId: String,
    nameToShow: String,
    components: List[Component],
    nextStep: Option[StepCurrentConfig]
)