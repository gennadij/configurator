package models.bo

import models.status.component.StatusComponent

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.02.2018
 */
case class ComponentBO (
    componentId: String,
    nameToShow: String,
    status : StatusComponent,
    excludeDependenciesOut: List[DependencyBO],
    excludeDependenciesIn: List[DependencyBO],
    requireDependenciesOut: List[DependencyBO],
    requireDependenciesIn: List[DependencyBO],
)