package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 11.11.2017
 */
class NextStepFinalStep extends Status{
  def message: String = "NEXt_STEP_FINAL_STEP"
  def status: String = "Die Konfiguration ist abgeschlossen"
}