package org.shared.status.selectedComponent

import org.shared.status.common.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 14.02.2018
 */
sealed abstract class StatusComponentType extends Status

case class DefaultComponent() extends StatusComponentType {
  def status: String = "DEFAULT_COMPONENT"
  def message: String = ""
}

case class FinalComponent() extends StatusComponentType {
  def status: String = "FINAL_COMPONENT"
  def message: String = ""
}

case class UnknowComponentType() extends StatusComponentType {
  def status: String = "ERROR_COMPONENT"
  def message: String = ""
}