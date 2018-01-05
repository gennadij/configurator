package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.11.2017
 */
abstract class Status {
  def status: String
  def message: String
}

case class FinalComponent() extends Status {
  def status: String = "FINAL_COMPONENT"
  def message: String = "Es wurde letze Komponente ausgewaelt. Konfiguration ist abgeschlossen"
}

case class ClassCastError() extends Status{
  def status: String = "CLASS_CAST_ERROR"
  def message: String = "Interner Fehler des Configurators"
}

case class ODBReadError() extends Status{
  def status: String = "NEXT_STEP_ODB_WRITE_ERROR"
  def message: String = "Beim Laden vom naechsten Schritt ist einen Fehler in Datenbank aufgetreten"
}