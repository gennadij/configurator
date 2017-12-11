package models.status.nextStep

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 11.11.2017
 */
class FinalStepSuccessful extends Status{
  def status: String = "NEXt_STEP_FINAL_STEP"
  def message: String = "Die Konfiguration ist abgeschlossen"
}