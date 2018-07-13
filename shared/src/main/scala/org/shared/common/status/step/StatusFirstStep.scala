package org.shared.common.status.step

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.02.2018
 */

sealed abstract class StatusFirstStep extends Status

case class FirstStepNotExist() extends StatusFirstStep{
  def status: String = "FIRST_STEP_NOT_EXIST"
  def message: String = ""
}

case class FirstStepExist() extends StatusFirstStep{
  def status: String = "FIRST_STEP_EXIST"
  def message: String = ""
}

case class MultipleFirstSteps() extends StatusFirstStep{
  def status: String = "MULTIPLE_FIRST_STEPS"
  def message: String = ""
}

case class CommonErrorFirstStep() extends StatusFirstStep{
  def status: String = "COMMON_ERROR_FIRST_STEP"
  def message: String = ""
}
