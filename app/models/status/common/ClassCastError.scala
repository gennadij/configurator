package models.status.common

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.11.2017
 */
class ClassCastError extends Status{
  def status: String = "CLASS_CAST_ERROR"
  def message: String = "Interner Fehler des Configurators"
}