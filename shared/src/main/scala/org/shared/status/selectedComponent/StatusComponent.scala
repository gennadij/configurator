package org.shared.status.selectedComponent

import org.shared.status.common.Status

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 *
 * Created by Gennadi Heimann 03.01.2018
 */
case class StatusComponent (
                             selectionCriterium: Option[StatusSelectionCriterium] = None,
                             selectedComponent: Option[StatusSelectedComponent] = None,
                             excludeDependency: Option[StatusExcludeDependency] = None,
                             common: Option[Status] = None,
                             componentType: Option[StatusComponentType] = None
)
