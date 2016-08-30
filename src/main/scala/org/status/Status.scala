package org.status

abstract class Status {
  def message: String
}

case class SuccessfulStatus (message: String) extends Status 

case class ErrorStatus(message: String) extends Status

case class WarningStatus(message: String) extends Status