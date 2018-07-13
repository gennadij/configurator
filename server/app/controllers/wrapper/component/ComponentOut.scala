package models.wrapper.component

import models.bo.DependencyBO
import org.shared.component.status.StatusComponent

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class ComponentOut (
    selectedComponentId: String = "",
    stepId: String = "",
    status: StatusComponent,
    dependencies: List[DependencyBO] = List()
)