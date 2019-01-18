package org.shared.status.common

import org.shared.status.currentConfig.StatusCurrentConfig
import org.shared.status.nextStep.StatusNextStep
import org.shared.status.selectedComponent.StatusCurrentStep

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 20.02.2018
 */
case class StatusStep(
                       startConfig: Option[Any] = None,
                       nextStep: Option[StatusNextStep] = None,
                       currentStep: Option[StatusCurrentStep] = None,
                       currentConfig: Option[StatusCurrentConfig] = None,
                       common: Option[Status] = None
)
