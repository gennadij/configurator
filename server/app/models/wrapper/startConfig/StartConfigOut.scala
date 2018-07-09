package models.wrapper.startConfig

import models.status.step.StatusStep
import models.bo.StepBO
import models.bo.ComponentBO

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 27.10.2017
 */
case class StartConfigOut (
    step: StepBO,
    components: List[ComponentBO]
)