package models.status.common

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
class ExcludeComponent extends Status{
  def status: String = "EXCLUDE_COMPONENT"
  def message: String = "Komponent darf nicht verwendet werden"
}