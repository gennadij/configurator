package models.wrapper.component

import models.wrapper.dependency.Dependency
import models.status.component.StatusComponent
import models.status.Status
import models.status.Status
import models.bo.DependencyBO

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