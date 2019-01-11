package org.shared.error

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.01.2019
  */
sealed abstract class Error (
                            val message: String,
                            val name: String,
                            val code: String
                            )
//Action Step = 01
//Action SelectedComponent = 02
//Unbekannt = 03
//Datenbank = 04
case class StepNotExist(stepId: String = "undefined")  extends Error(
  message = s"Der aufgeforderte Schritt (stepId = $stepId) exestiert nicht in dem Datenbank",
  name ="STEP_NOT_EXIST",
  code = "E010001")

case class MultipleSteps(id: String = "undefined") extends Error(
  message = s"Es wurde mehre Schritte ausgehend aus der id = $id gefunden",
  name = "MULTIPLE_STEPS",
  code = "E010002"
)

case class Unknow() extends Error(
  message = "Unbekannter Fehler",
  name = "UNKNOW",
  code = "E030001"
)

case class ClassCast() extends Error(
  message = "Der Objekt aus der Datenbank kann nicht gecastet werden",
  name ="CLASS_CAST",
  code = "E040001"
)

case class ODBRead() extends Error(
  message = "Beim Lesen aus der Datenbank einen Fehler ausgetretten",
  name ="ODB_READ",
  code = "E040002"
)

case class ODBConnection() extends Error(
  message = "Es wurde keine Verbindung zu der Datenbank hergestellt",
  name ="ODB_CONNECTION",
  code = "E040003"
)