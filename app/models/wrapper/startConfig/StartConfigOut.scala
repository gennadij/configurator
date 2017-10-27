package models.wrapper.startConfig

import models.wrapper.common.Step

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 27.10.2017
 */
case class StartConfigOut (
    status: Boolean,
    message: String,
    step: Step
)