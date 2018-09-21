package org.shared.common.status.step

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.02.2018
 */
sealed abstract class StatusCurrentStep extends Status

case class CurrentStepNotExist() extends StatusCurrentStep{
  def status: String = "FATHER_STEP_NOT_EXIST"
  def message: String = ""
}

case class CurrentStepExist() extends StatusCurrentStep{
  def status: String = "FATHER_STEP_EXIST"
  def message: String = ""
}

case class MultipleCurrentSteps() extends StatusCurrentStep{
  def status: String = "MULTIPLE_FATHER_STEPS"
  def message: String = ""
}

case class CommonErrorCurrentStep() extends StatusCurrentStep{
  def status: String = "COMMON_ERROR_FATHER_STEP"
  def message: String = ""
}
