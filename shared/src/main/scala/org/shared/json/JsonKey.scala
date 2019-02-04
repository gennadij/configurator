package org.shared.json

/**
  * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
  *
  * Created by Gennadi Heimann 19.12.2018
  */
object JsonKey {
  //Common
  def result = "result"
  def params = "params"
  def json = "json"
  def nameToShow = "nameToShow"

  //error
  def errors = "errors"
  def name = "name"
  def code = "code"
  def warnings = "warnings"

  //Warning
  def excludedComponentInternal = "excludedComponentInternal"
  def excludedComponentExternal = "excludedComponentExternal"

  //Status Common
  def status = "status"
  def common = "common"
  def message = "message"

  //Status SelectedComponent
  def selectionCriterion = "selectionCriterion"
  def selectedComponent = "selectedComponent"
  def excludeDependencyInternal = "excludeDependencyInternal"
  def excludeDependencyExternal = "excludeDependencyExternal"
  def componentType = "componentType"

  //Status Step
  def firstStep = "firstStep"
  def nextStep = "nextStep"
  def currentStep = "currentStep"
  def currentConfig = "currentConfig"

  //Step
  def configUrl = "configUrl"
  def step = "step"
  def stepId = "stepId"
  def components = "components"

  //Component
  def componentId = "componentId"
  def componentsForSelection = "componentsForSelection"

  //SelectedComponent
  def selectedComponentId = "selectedComponentId"
  def excludeDependenciesOut = "excludeDependenciesOut"
  def excludeDependenciesIn = "excludeDependenciesIn"
  def requireDependenciesOut = "requireDependenciesOut"
  def requireDependenciesIn = "requireDependenciesIn"
  def lastComponent = "lastComponent"
  def addedComponent = "addedComponent"
  def error = "errors"
  def warning = "warning"
  def info = "info"

  //Dependency
  def outId = "outId"
  def inId = "inId"
  def dependencyType = "dependencyType"
  def visualization = "visualization"
  def strategyOfDependencyResolver = "strategyOfDependencyResolver"





}
