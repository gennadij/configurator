package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.12.2017
 */
sealed abstract class ExcludeComponentStatus extends Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Dez 5, 2017
 */
case class ErrorComponent() extends ExcludeComponentStatus{
  def status: String = "ERROR_COMPONENT"
  def message: String = "Diese Komponente darf nicht ausgewaehlt werden. " + 
      "Sie wird von einer anderen Komponente ausgeschlossen. Bitte waehlen Sie eine andere Komponente"
}

case class SuccessComponent() extends ExcludeComponentStatus {
  def status: String = "SUCCESS_COMPONENT"
  def message: String = "Diese Komponente wurde erfolgreich zu der Knfiguration hinzugefuegt"
}

