package models.bo.step

import org.shared.error.Error
import org.shared.warning.Warning

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 07.02.2019
  */
case class StepContainerBO(
                            configUrl: Option[String] = None,
                            step: Option[StepBO] = None,
                            componentsForSelection: Option[List[ComponentForSelectionBO]] = None,
                            error: Option[List[Error]] = None,
                            warnings: Option[List[Warning]] = None
                         )
