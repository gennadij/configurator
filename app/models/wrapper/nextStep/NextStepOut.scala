package models.wrapper.nextStep

import models.wrapper.common.Step

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Oct 31, 2017
 */
case class NextStepOut (
    status: Boolean,
    message: String,
    step: Step
)