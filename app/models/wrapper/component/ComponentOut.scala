package models.wrapper.component

import models.wrapper.dependency.Dependency

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 13.11.2017
 */
case class ComponentOut (
    status: String,
    message: String,
    nextStepExistence: Boolean,
    dependencies: List[Dependency]
)