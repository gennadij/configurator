package models.status.common

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 21.11.2017
 */
class Error extends Status{
  def status: String = "ERROR"
  def message: String = "Action war nicht erfolgreich"
}