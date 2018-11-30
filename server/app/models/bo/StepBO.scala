package models.bo

import org.shared.status.common.StatusStep

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
    status: Option[StatusStep] = None,
    componentIds: Option[List[String]] = None
)