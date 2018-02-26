package models.status.step

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 20.02.2018
 */
case class StatusStep(
    firstStep: Option[StatusFirstStep],
    nextStep: Option[StatusNextStep],
    fatherStep: Option[StatusFatherStep],
    common: Option[Status]
)