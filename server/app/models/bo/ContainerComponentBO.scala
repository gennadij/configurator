package models.bo

import org.shared.component.status.StatusComponent

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 09.02.2018
  */
case class ContainerComponentBO(
                        status: Option[StatusComponent] = None,
                        components: List[ComponentBO] = List()
                      )