package org.shared.status.currentConfig

import org.shared.status.common.Status

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 10.10.2018
  */

sealed abstract class StatusCurrentConfig extends Status

case class StepCurrentConfigSuccess() extends StatusCurrentConfig {
  def status: String = "STEP_CURRENT_CONFIG_SUCCESS"
  def message: String = ""
}

case class StepCurrentConfigBOIncludeNoSelectedComponents() extends StatusCurrentConfig {
  def status: String = "STEP_CURRENT_CONFIG_BO_INCLUDE_NO_SELECTED_COMPONENTS"
  def message: String = ""
}
