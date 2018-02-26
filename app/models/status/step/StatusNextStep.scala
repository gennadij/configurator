package models.status.step

import models.status.Status

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