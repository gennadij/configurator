package models.status.Component

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
class RequireNextStep extends Status{
  def status: String = "REQUIRE_NEXT_STEP"
  def message: String = "Es darf keine weitere Komponente ausgewaelt werden und es muss naechste Schhritt geladen werden"
}