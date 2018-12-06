package org.shared.status.selectedComponent

import org.shared.status.common.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 04.01.2018
 */

sealed abstract class StatusExcludeDependency extends Status

case class ExcludedComponent(
                              excludeComponent: String,
                              excludedComponent: String) extends StatusExcludeDependency() {
  def status: String = "EXCLUDED_COMPONENT"
  def message: String = s"Die Komponent $excludedComponent wird von der Komponente $excludeComponent ausgeschlossen."
}

case class NotExcludedComponent() extends StatusExcludeDependency() {
  def status: String = "NOT_EXCLUDED_COMPONENT"
  def message: String = "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
}