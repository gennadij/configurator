package models.status.common

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 19, 2017
 */
class Successful extends Status{
  def status: String = "SUCCESSFUL"
  def message: String = "Action war erfolgreich"
}