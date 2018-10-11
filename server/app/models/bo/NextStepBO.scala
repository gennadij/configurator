package models.bo

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 27.07.2018
  */
case class NextStepBO (
                        step: Option[StepBO] = None,
                        componentsForSelection: Option[ComponentsForSelectionBO] = None
                      )
