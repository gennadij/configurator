package models.bo

import org.shared.common.status.step.StatusStep

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.02.2018
 */
case class StepBO (
    stepId: Option[String] = None,
    nameToShow: Option[String] = None,
    selectionCriteriumMin: Option[Int] = None,
    selectionCriteriumMax: Option[Int] = None,
    status: StatusStep,
    componentIds: Option[List[String]] = None
)