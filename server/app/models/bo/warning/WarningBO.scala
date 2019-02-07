package models.bo.warning

import org.shared.warning.Warning

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 25.01.2019
  */
case class WarningBO(
                      excludedComponentExternal: Option[Warning] = None,
                      excludedComponentInternal: Option[Warning] = None
                    )
