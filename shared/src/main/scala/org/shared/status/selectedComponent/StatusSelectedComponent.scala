package org.shared.status.selectedComponent

import org.shared.status.common.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
sealed abstract class StatusSelectedComponent extends Status

case class RemovedComponent() extends StatusSelectedComponent {
  def status: String = "REMOVED_COMPONENT"
  def message: String = "Die Komponente wurde erfolgreich aus der aktuellen Konfiguration entfernt"
}

case class AddedComponent() extends StatusSelectedComponent {
  def status: String = "ADDED_COMPONENT"
  def message: String = "Die Komponente wurde erfolgreich in der aktuelle Konfiguration hinzugefuegt"
}

case class ErrorSelectedComponent() extends StatusSelectedComponent {
  def status: String = "ERROR_COMPONENT"
  def message: String = "Die Komponente darf nicht in der aktuelle Konfiguration hinzugefuegt"
}

case class NotAllowedComponent() extends StatusSelectedComponent {
  def status: String = "NOT_ALLOWED_COMPONENT"
  def message: String = ""
}