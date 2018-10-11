package org.shared.component.status

import org.shared.common.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 04.01.2018
 */

sealed abstract class StatusExcludeDependency extends Status

case class ExcludedComponent() extends StatusExcludeDependency() {
  def status: String = "EXCLUDED_COMPONENT"
  def message: String = "Diese Komponente darf nicht ausgewaehlt werden. " + 
      "Sie wird von einer anderen Komponente ausgeschlossen. Bitte waehlen Sie eine andere Komponente"
}

case class NotExcludedComponent() extends StatusExcludeDependency() {
  def status: String = "NOT_EXCLUDED_COMPONENT"
  def message: String = "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
}