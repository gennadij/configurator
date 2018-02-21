package models.bo

import models.status.step.StatusStep

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.02.2018
 */
case class StepBO (
    stepId: String,
    nameToShow: String,
    selectionCriteriumMin: Int,
    selectionCriteriumMax: Int,
    status: StatusStep
)