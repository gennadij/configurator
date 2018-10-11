package models.bo

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 10.07.2018
  */
case class StartConfigBO (
                           configUrl: Option[String] = None,
                           step: Option[StepBO] = None,
                           componentsForSelection: Option[ComponentsForSelectionBO] = None

)