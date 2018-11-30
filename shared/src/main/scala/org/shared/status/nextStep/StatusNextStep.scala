package org.shared.status.nextStep

import org.shared.status.common.Status

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

case class NextMultipleSteps() extends StatusNextStep{
  def status: String = "NEXT_MULTIPLE_STEPS"
  def message: String = ""
}

case class NextStepCommonError() extends StatusNextStep{
  def status: String = "NEXT_STEP_COMMON_ERROR"
  def message: String = ""
}



case class NextStepIncludeNoComponents() extends StatusNextStep{
  def status: String = "NEXT_STEP_INCLUDE_NO_COMPONENTS"
  def message: String = ""
}