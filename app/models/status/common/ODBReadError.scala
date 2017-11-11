package models.status.common

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Nov 11, 2017
 */
class ODBReadError extends Status{
  def status: String = "NEXT_STEP_ODB_WRITE_ERROR"
  def message: String = "Beim Laden vom naechsten Schritt ist einen Fehler in Datenbank aufgetreten"
}