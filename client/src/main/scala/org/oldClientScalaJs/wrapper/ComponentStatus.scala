package org.oldClientScalaJs.wrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 10.01.2018
 */
case class ComponentStatus (
    selectionCriterium: Option[Status],
    selectedComponent: Option[Status],
    excludeDependency: Option[Status],
    common: Option[Status],
    componentType: Option[Status]
)