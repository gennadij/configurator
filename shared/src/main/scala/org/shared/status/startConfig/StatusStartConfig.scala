package org.shared.status.startConfig

import org.shared.status.common.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.02.2018
 */

sealed abstract class StatusStartConfig extends Status

case class StartConfigNotExist() extends StatusStartConfig{
  def status: String = "FIRST_STEP_NOT_EXIST"
  def message: String = ""
}

case class StartConfigExist() extends StatusStartConfig{
  def status: String = "FIRST_STEP_EXIST"
  def message: String = ""
}

case class MultipleFirstSteps() extends StatusStartConfig{
  def status: String = "MULTIPLE_FIRST_STEPS"
  def message: String = ""
}

case class CommonErrorStartConfig() extends StatusStartConfig{
  def status: String = "COMMON_ERROR_FIRST_STEP"
  def message: String = ""
}

