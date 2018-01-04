package models.status.component

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 03.01.2018
 */


//object StatusComponent{
//  def addStatusSelectionCriterium(
//      selectionCriterium: StatusSelectionCriterium, 
//      statusComponent: StatusComponent): StatusComponent = {
//    statusComponent.copy(selectionCriterium = selectionCriterium)
//  }
//  
//  def addStatusSelectedComponent(
//      selectedComponent: StatusSelectedComponent, 
//      statusComponent: StatusComponent): StatusComponent = {
//    statusComponent.copy(selectedComponent = selectedComponent)
//  }
//}


case class StatusComponent (
    selectionCriterium: StatusSelectionCriterium,
    selectedComponent: StatusSelectedComponent,
    excludeDependency: StatusExcludeDependency,
    nextStepExistence: Boolean
)