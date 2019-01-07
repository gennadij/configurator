package org.shared.status.startConfig

import org.shared.status.common.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 26.02.2018
 */

//TODO Alle Stati als Beispiel models.bo.types.StrategyOfDR implementieren
//TODO Parameter <demostaration (true/false)> implementieren.
//TODO Werte der Stati müssen in dem DB gehaltet werden und bei der Start des Konfigurators gelesen werden.
//TODO Jede Status muss einen FehlerCode enthalten.  Z.B. E000000
//TODO Erste 2 Zahlen des Fehlerkodes ist die Aktion (Z.B SelectedComponent), die
//TODO die restlichen 4 Fehler selbst.

sealed abstract class StatusStartConfig extends Status

case class StartConfigNotExist() extends StatusStartConfig{
  def status: String = "FIRST_STEP_NOT_EXIST"
  def message: String = ""

}

case class StartConfigExist() extends StatusStartConfig{
  def status: String = "FIRST_STEP_EXIST"
  def message: String = ""
}

case class MultipleFirstSteps() extends StatusStartConfig{
  def status: String = "MULTIPLE_FIRST_STEPS"
  def message: String = ""
}

//TODO prüfen ob gebraucht wird
case class CommonErrorStartConfig() extends StatusStartConfig{
  def status: String = "COMMON_ERROR_FIRST_STEP"
  def message: String = ""
}

