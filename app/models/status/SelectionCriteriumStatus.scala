package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.11.2017
 */
sealed abstract class SelectionCriteriumStatus{
  def status: String
  def message: String
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
case class ExcludeComponent() extends SelectionCriteriumStatus{
  def status: String = "EXCLUDE_COMPONENT"
  def message: String = "Komponent darf nicht verwendet werden"
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
case class RequireComponent() extends SelectionCriteriumStatus{
  def status: String = "REQUIRE_COMPONENT"
  def message: String = "Es muss weitere Komponente ausgewaelt werden um nächste Schritt zu laden"
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
case class RequireNextStep() extends SelectionCriteriumStatus{
  def status: String = "REQUIRE_NEXT_STEP"
  def message: String = "Es darf keine weitere Komponente ausgewaelt werden und es muss naechste Schhritt geladen werden"
}

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 21.11.2017
 */
case class AllowNextComponent() extends SelectionCriteriumStatus{
  def status: String = "ALLOW_NEXT_COMPONENT"
  def message: String = ""
}