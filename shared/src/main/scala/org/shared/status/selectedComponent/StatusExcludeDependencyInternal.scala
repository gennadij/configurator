package org.shared.status.selectedComponent

import org.shared.status.common.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 04.01.2018
 */

sealed abstract class StatusExcludeDependencyInternal extends Status

case class ExcludedComponentInternal() extends StatusExcludeDependencyInternal {
  def status: String = "EXCLUDED_COMPONENT_INTERNAL"
  def message: String = s"Die Komponent darf nich ausgewählt werden."
}

case class NotExcludedComponentInternal() extends StatusExcludeDependencyInternal {
  def status: String = "NOT_EXCLUDED_COMPONENT_INTERNAL"
  def message: String = "Diese Komponente kann zu der Konfiguration hinzugefuegt werden"
}