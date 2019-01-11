package models.bo

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 10.07.2018
  */
case class ComponentBO(
                      componentId: Option[String] = None,
                      nameToShow: Option[String] = None,
                      excludeDependenciesOut: Option[List[DependencyBO]] = None,
                      excludeDependenciesIn: Option[List[DependencyBO]] = None,
                      requireDependenciesOut: Option[List[DependencyBO]] = None,
                      requireDependenciesIn: Option[List[DependencyBO]] = None,
                      permissionToSelection: Option[Boolean] = None
                    )
