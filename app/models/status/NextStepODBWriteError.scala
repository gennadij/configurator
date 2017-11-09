package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.11.2017
 */
class NextStepODBWriteError extends Status{
  def message: String = "NEXt_STEP_ODB_WRITE_ERROR"
  def status: String = "Beim Laden vom naechsten Schritt ist einen Fehler in Datenbank aufgetreten"
}