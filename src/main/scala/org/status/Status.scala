package org.status

abstract class Status {
  def message: String
}

case class SuccessfulStatus (message: String, id: String) extends Status 

case class ErrorStatus(message: String, id: String) extends Status

case class WarningStatus(message: String, id: String) extends Status