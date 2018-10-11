package org.shared.common.status.step

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 20.02.2018
 */
case class StatusStep(
                       startConfig: Option[StatusStartConfig] = None,
                       nextStep: Option[StatusNextStep] = None,
                       currentStep: Option[StatusCurrentStep] = None,
                       currentConfig: Option[StatusCurrentConfig] = None,
                       common: Option[Status] = None
)