package models.bo

import org.shared.status.selectedComponent.StatusComponent


/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 16.07.2018
  */
case class ComponentsForSelectionBO(
                                     status: Option[StatusComponent] = None,
                                     components: List[ComponentBO] = List()
                             )
