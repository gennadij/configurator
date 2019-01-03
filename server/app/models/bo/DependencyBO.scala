package models.bo

import models.bo.types.StrategyOfDR

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 09.02.2018
 */
case class DependencyBO (
    outId: String,
    inId: String,
    visualization: String,
    dependencyType: String,
    nameToShow: String,
    strategyOfDependencyResolver: StrategyOfDR
)