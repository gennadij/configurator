package models.status.component

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
sealed abstract class StatusSelectedComponent extends Status 

case class RemoveComponent() extends StatusSelectedComponent {
  def status: String = "REMOVE_COMPONENT"
  def message: String = "Die Komponente wurde erfolgreich aus der aktuellen Konfiguration entfernt"
}

case class AddComponent() extends StatusSelectedComponent {
  def status: String = "ADD_COMPONENT"
  def message: String = "Die Komponente wurde erfolgreich in der aktuelle Konfiguration hinzugefuegt"
}

//case class ErrorComponent() extends StatusSelectedComponent {
//  def status: String = "ERROR_COMPONENT"
//  def message: String = "Die Komponente darf nicht in der aktuelle Konfiguration hinzugefuegt"
//}