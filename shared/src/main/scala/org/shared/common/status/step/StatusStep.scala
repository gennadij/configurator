package org.shared.common.status.step

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 20.02.2018
 */
case class StatusStep(
    firstStep: Option[StatusFirstStep] = None,
    nextStep: Option[StatusNextStep] = None,
    fatherStep: Option[StatusFatherStep] = None,
    common: Option[Status] = None
)