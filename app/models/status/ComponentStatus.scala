package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.12.2017
 */
sealed abstract class ComponentStatus extends Status

case class ErrorComponent() extends ComponentStatus{
  def status: String = "ERROR_COMPONENT"
  def message: String = "Diese Komponente darf nicht ausgewaehlt werden. " + 
      "Sie wird von einer anderen Komponente ausgeschlossen. Bitte waehlen Sie eine andere Komponente"
}

case class SuccessComponent() extends ComponentStatus {
  def status: String = "SUCCESS_COMPONENT"
  def message: String = "Diese Komponente wurde erfolgreich zu der Knfiguration hinzugefuegt"
}

case class FinalComponent() extends ComponentStatus {
  def status: String = "FINAL_COMPONENT"
  def message: String = "Es wurde letze Komponente ausgewaelt. Konfiguration ist abgeschlossen"
}
