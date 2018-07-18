package models.wrapper.nextStep

import models.bo.StepBO
import models.bo.SelectedComponentBO

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann Oct 31, 2017
 */
case class NextStepOut (
    step: StepBO,
    components: List[SelectedComponentBO]
)