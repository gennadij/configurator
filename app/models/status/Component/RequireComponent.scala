package models.status.Component

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
class RequireComponent extends Status{
  def status: String = "REQUIRE_COMPONENT"
  def message: String = "Es muss weitere Komponente ausgewaelt werden um nächste Schritt zu laden"
}