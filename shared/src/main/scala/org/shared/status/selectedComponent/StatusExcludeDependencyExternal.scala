package org.shared.status.selectedComponent

import org.shared.status.common.Status


/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 18.12.2018
  */
sealed abstract class StatusExcludeDependencyExternal extends Status

case class ExcludedComponentExternal(
                                      excludeComponents: List[String],
                                      excludedComponents: List[String])
  extends StatusExcludeDependencyExternal {

  def status: String = "EXCLUDED_COMPONENT_EXTERNAL"
  def message: String = createNameToShowForComponents(excludeComponents)


  private def createNameToShowForComponents(excludeComponents: List[String]): String = {
    (excludeComponents map (eC => {
      s"Diese Komponente $eC verbietet die Auswahl von aktueller Komponente"
    })).toString()
  }
}

case class NotExcludedComponentExternal() extends StatusExcludeDependencyExternal {
  def status: String = "EXCLUDED_COMPONENT_EXTERNAL"
  def message: String = s"Die Komponente wird nicht ausgeschlossen"
}

