package models.status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.12.2017
 */
sealed abstract class StartConfigStatus extends Status

case class StartConfigSuccessful() extends StartConfigStatus{
    def status = "START_CONFIG_SUCCESSFUL"
    def message = "Der erste Schritt wurde erfolgreich geladen"
}