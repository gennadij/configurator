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
  def step = "step"
  def stepId = "stepId"
  def components = "components"

  //Component
  def componentId = "componentId"

  //SelectedComponent
  def selectedComponentId = "selectedComponentId"
  def excludeDependenciesOut = "excludeDependenciesOut"
  def excludeDependenciesIn = "excludeDependenciesIn"
  def requireDependenciesOut = "requireDependenciesOut"
  def requireDependenciesIn = "requireDependenciesIn"

  //Dependency
  def outId = "outId"
  def inId = "inId"
  def dependencyType = "dependencyType"
  def visualization = "visualization"





}
