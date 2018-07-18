package org.oldClientScalaJs.wrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.03.2018
 */
case class StepStatus (
    firstStep: Option[Status] = None,
    nextStep: Option[Status] = None,
    fatherStep: Option[Status] = None,
    common: Option[Status] = None
)