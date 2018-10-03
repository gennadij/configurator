package org.shared.common.status.step

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 20.02.2018
 */
sealed abstract class StatusNextStep extends Status

case class NextStepNotExist() extends StatusNextStep{
  def status: String = "NEXT_STEP_NOT_EXIST"
  def message: String = ""
}

case class NextStepExist() extends StatusNextStep{
  def status: String = "NEXT_STEP_EXIST"
  def message: String = ""
}

case class MultipleNextSteps() extends StatusNextStep{
  def status: String = "MULTIPLE_NEXT_STEPS"
  def message: String = ""
}

case class CommonErrorNextStep() extends StatusNextStep{
  def status: String = "COMMON_ERROR_NEXT_STEP"
  def message: String = ""
}

case class StepCurrentConfigBOIncludeNoSelectedComponents() extends StatusNextStep{
  def status: String = "STEP_CURRENT_CONFIG_BO_INCLUDE_NO_SELECTED_COMPONENTS"
  def message: String = ""
}

case class NextStepIncludeNoComponents() extends StatusNextStep{
  def status: String = "NEXT_STEP_INCLUDE_NO_COMPONENTS"
  def message: String = ""
}