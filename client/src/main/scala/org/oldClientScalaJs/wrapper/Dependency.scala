package org.oldClientScalaJs.wrapper

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 21.12.2017
 */
case class Dependency(
    outId: String,
    outIdRow: String,
    inId: String,
    inIdRow: String,
    dependencyType: String,
    visualization: String,
    nameToShow: String
)