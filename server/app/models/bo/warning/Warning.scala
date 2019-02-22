package org.shared.warning

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 18.01.2019
  */
sealed abstract class Warning (
                                val message: String,
                                val name: String,
                                val code: String
                              )

//excluded 01
//require 02

//TODO definiren die Texte für alle Infos
case class ExcludedComponentInternal() extends Warning(
  message = "Die Komponente wird von der anderen Komponente in diesem Schritt ausgeschlossen",
  name = "EXCLUDED_COMPONENT_INTERNAL",
  code = "W010001"
)

case class ExcludedComponentExternal() extends Warning(
  message = "Die Komponente wird von der anderen Komponente in anderem Schritt ausgeschlossen",
  name = "EXCLUDED_COMPONENT_EXTERNAL",
  code = "W010002"
)

case class ExcludeComponentInternal(excludedComponentId: List[String] = List()) extends Warning(
  message = "Ausgeschlossene Komponente " + excludedComponentId.mkString(sep = ", ") + ".",
  name = "EXCLUDE_COMPONENT_INTERNAL",
  code = "W010003"
)



case class ExcludeComponentExternal(excludedComponentId: List[String] = List()) extends Warning(
  message = "Die ausgewählte Komponente schließt andere Komponente (" + excludedComponentId.mkString(sep = ", ")  +") aus",
  name = "EXCLUDE_COMPONENT_EXTERNAL",
  code = "W010003"
)