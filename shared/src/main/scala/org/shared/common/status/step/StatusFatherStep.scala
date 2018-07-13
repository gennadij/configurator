package org.shared.common.status.step

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.02.2018
 */
sealed abstract class StatusFatherStep extends Status

case class FatherStepNotExist() extends StatusFatherStep{
  def status: String = "FATHER_STEP_NOT_EXIST"
  def message: String = ""
}

case class FatherStepExist() extends StatusFatherStep{
  def status: String = "FATHER_STEP_EXIST"
  def message: String = ""
}

case class MultipleFatherSteps() extends StatusFatherStep{
  def status: String = "MULTIPLE_FATHER_STEPS"
  def message: String = ""
}

case class CommonErrorFatherStep() extends StatusFatherStep{
  def status: String = "COMMON_ERROR_FATHER_STEP"
  def message: String = ""
}
