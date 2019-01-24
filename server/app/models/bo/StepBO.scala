package models.bo

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.02.2018
 */

case class StepBO (
                    stepId: Option[String] = None,
                    nameToShow: Option[String] = None,
                    selectionCriterionMin: Option[Int] = None,
                    selectionCriterionMax: Option[Int] = None
)