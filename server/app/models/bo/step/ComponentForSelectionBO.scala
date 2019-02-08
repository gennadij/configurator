package models.bo.step

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 08.02.2019
  */
case class ComponentForSelectionBO(
                                   componentId: Option[String] = None,
                                   nameToShow: Option[String] = None,
                                   permissionToSelection: Option[Boolean] = None
                                 )
