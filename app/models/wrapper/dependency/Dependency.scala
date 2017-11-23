package models.wrapper.dependency

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 12.11.2017
 */
case class Dependency (
    fromId: String,
    toId: String,
    visualization: String,
    dependencyType: String,
    nameToShow: String
)