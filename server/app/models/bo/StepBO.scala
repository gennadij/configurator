package models.bo

import org.shared.status.common.StatusStep

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.02.2018
 */

//TODO und aus dem StepBO.status entfernen, nachdem NextStep implementiert wurde
case class StepBO (
                    stepId: Option[String] = None,
                    nameToShow: Option[String] = None,
                    selectionCriterionMin: Option[Int] = None,
                    selectionCriterionMax: Option[Int] = None,
                    status: Option[StatusStep] = None, //TODO DELETE
                    componentIds: Option[List[String]] = None
)