package org.shared.nextStep

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 12.12.2017
 */
sealed abstract class NextStepStatus extends Status

case class NextStepSuccessful() extends NextStepStatus{
  def status: String = "NEXT_STEP_SUCCESSFUL"
  def message: String = "Der naechste Schritt wurde erfolgreich geladen"
}

case class NextStepError() extends NextStepStatus{
  def status: String = "NEXT_STEP_ERROR"
  def message: String = "Der naechste Schritt wurde nicht erfolgreich geladen"
}

case class FinalStepSuccessful() extends NextStepStatus{
  def status: String = "FINAL_STEP_SUCCESSFUL"
  def message: String = "Die Konfiguration ist abgeschlossen"
}

case class CurrentConfigInconsistent() extends NextStepStatus{
  def status: String = "CURRENT_CONFIG_INCONSISTENT"
  def message: String = ""
}

case class CurrentConfigConsistent() extends NextStepStatus{
  def status: String = "CURRENT_CONFIG_CONSISTENT"
  def message: String = ""
}