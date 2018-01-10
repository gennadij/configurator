package models.status.component

import models.status.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */
case class StatusComponent (
    selectionCriterium: Option[StatusSelectionCriterium],
    selectedComponent: Option[StatusSelectedComponent],
    excludeDependency: Option[StatusExcludeDependency],
    common: Option[Status],
    nextStepExistence: Option[Boolean]
)