package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.11.2017
 */
class StartConfigODBWriteError extends Status{
  def message: String = "START_CONFIG_ODB_WRITE_ERROR"
  def status: String = "Beim Laden vom ersten Schrit ist einen Fehler in Datenbank aufgetreten"
}