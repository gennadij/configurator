package models.status.startCongig

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.11.2017
 */
class StartConfigSuccessful extends Status{
    def status = "START_CONFIG_SUCCESSFUL"
    def message = "Der erste Schritt wurde erfolgrei geladen"
}