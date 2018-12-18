package org.shared.status.selectedComponent

import org.shared.status.common.Status

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 18.12.2018
  */
sealed abstract class StatusExcludeDependencyExternal extends Status

case class ExcludedComponentExternal(
                                      excludeComponent: String,
                                      excludedComponent: String)
  extends StatusExcludeDependencyExternal {

  def status: String = "EXCLUDED_COMPONENT_EXTERNAL"
  def message: String = s"Die Komponent $excludedComponent wird von der " +
    "Komponente $excludeComponent ausgeschlossen."
}

case class NotExcludedComponentExternal() extends StatusExcludeDependencyExternal {
  def status: String = "EXCLUDED_COMPONENT_EXTERNAL"
  def message: String = s"Die Komponente wird nicht ausgeschlossen"
}

