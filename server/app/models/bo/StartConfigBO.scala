package models.bo

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 10.07.2018
  */

//TODO Separate Parameter für den Status
//status: StatusStartConfig
//TODO und aus dem StepBO.status entfernen

case class StartConfigBO (
                           configUrl: Option[String] = None,
                           step: Option[StepBO] = None,
                           componentsForSelection: Option[ComponentsForSelectionBO] = None

)
