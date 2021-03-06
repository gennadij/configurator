package org.shared.info

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 25.01.2019
  */

sealed abstract class Info (val message: String,
            val name: String,
            val code: String)

//SelectionCriterion 01
case class RequireComponent() extends Info(
  message = "In diesem Schritt müssen weitere Komponente ausgewählt werden.",
  name = "REQUIRE_COMPONENT",
  code = "I010001"
)

case class RequireNextStep() extends Info(
  message = "Es darf keine weitere Komponente ausgewählt werden. Es muss nächste Schritt geladen werden",
  name = "REQUIRE_NEXT_STEP",
  code = "I010002"
)

case class AllowNextComponent() extends Info(
  message = "Man darf weitere Komponente auswählen",
  name = "ALLOW_NEXT_COMPONENT",
  code = "I010003"
)

case class NotAllowedComponent() extends Info(
  message = "Die Komponente wird nich in der aktuelle Konfiguration hinzugefügt",
  name = "NOT_ALLOWED_COMPONENT",
  code = "I010004"
)
