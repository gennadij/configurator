package models.bo.currentConfig

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2019
  */
case class CurrentConfigContainerBO(
                                  var currentConfig: Option[CurrentConfigStepBO] = None
                                  )
