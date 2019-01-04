package models.persistence

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 
  */
object PropertyKeys {
  // TODO SelectionCriterion im DB und in Persistence anpassen
  val CONFIG_URL = "configUrl"
  val ADMIN_USER = "AdminUser"
  val COMPONENT = "Component"
  val ADMIN_ID = "adminId"
  val COMPONENT_ID = "componentId"
  val KIND = "kind"
  val NAME_TO_SHOW: String = "nameToShow"
  val HAS_COMPONENT: String = "hasComponent"
  val HAS_STEP: String = "hasStep"
  val HAS_DEPENDENCY = "hasDependency"
  val IN: String = "in"
  val OUT: String = "out"
  val VISUALIZATION: String = "visualization"
  val DEPENDENCY_TYPE: String = "dependencyType"
  val SELECTION_CRITERIUM_MIN: String = "selectionCriteriumMin"
  val SELECTION_CRITERIUM_MAX: String = "selectionCriteriumMax"
  val EXCLUDE: String = "exclude"
  val REQUIRE: String = "require"
  val STRATEGY_OF_DEPENDENCY_RESOLVER = "strategyOfDependencyResolver"
}
