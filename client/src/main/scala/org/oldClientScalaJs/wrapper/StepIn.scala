package org.oldClientScalaJs.wrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 22.12.2017
 */
case class StepIn (
    status: StepStatus,
    stepId: String,
    stepIdRow: String,
    nameToShow: String,
    components: List[Component]
)