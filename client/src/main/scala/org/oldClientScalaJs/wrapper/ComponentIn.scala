package org.oldClientScalaJs.wrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
case class ComponentIn (
    selectedComponentId: String,
    stepId: String,
    status: ComponentStatus,
    dependencies: List[Dependency]
)