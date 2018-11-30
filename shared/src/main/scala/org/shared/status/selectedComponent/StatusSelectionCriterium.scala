package org.shared.status.selectedComponent

import org.shared.status.common.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
sealed abstract class StatusSelectionCriterium extends Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
case class NotAllowNextComponent() extends StatusSelectionCriterium {
  def status: String = "NOT_ALLOW_NEXT_COMPONENT"
  def message: String = "Komponent darf nicht verwendet werden"
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
case class RequireComponent() extends StatusSelectionCriterium{
  def status: String = "REQUIRE_COMPONENT"
  def message: String = "Es muss weitere Komponente ausgewaelt werden um nächste Schritt zu laden"
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
case class RequireNextStep() extends StatusSelectionCriterium{
  def status: String = "REQUIRE_NEXT_STEP"
  def message: String = "Es darf keine weitere Komponente ausgewaelt werden und es muss naechste Schhritt geladen werden"
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 21.11.2017
 */
case class AllowNextComponent() extends StatusSelectionCriterium{
  def status: String = "ALLOW_NEXT_COMPONENT"
  def message: String = "Sie koennen weitere Komponente auswaelen"
}

case class ErrorSelectionCriterion() extends StatusSelectionCriterium{
  def status: String = "ERROR_SELECTION_CRITERION"
  def message: String = ""
}