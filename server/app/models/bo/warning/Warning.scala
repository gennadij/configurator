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
case class ExcludedComponentInternal() extends Warning(
  message = "",
  name = "EXCLUDED_COMPONENT_INTERNAL",
  code = "W010001"
)
//TODO ausschliessende Komponent Id
case class ExcludedComponentExternal() extends Warning(
  message = "",
  name = "EXCLUDED_COMPONENT_EXTERNAL",
  code = "W010002"
)

case class ExcludeComponentInternal(excludedComponentId: List[String] = List()) extends Warning(
  message = "Ausgeschlossene Komponente " + excludedComponentId.mkString(sep = ", ") + ".",
  name = "EXCLUDE_COMPONENT_EXTERNAL",
  code = "W010003"
)

//TODO es soll eine HashId sein

case class ExcludeComponentExternal(excludedComponentId: List[String] = List()) extends Warning(
  message = "Ausgeschlossene Komponente " + excludedComponentId.mkString(sep = ", ") + ".",
  name = "EXCLUDE_COMPONENT_EXTERNAL",
  code = "W010003"
)