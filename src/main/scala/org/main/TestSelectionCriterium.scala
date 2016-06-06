package org.main

import org.configMgr.ConfigMgr
import org.configTree.component._

class TestSelectionCriterium {
  
  def selectCriterium = ConfigMgr.getNextStep(Set(new SelectedComponent("001001"), new SelectedComponent("001002")))
  
//  def getStepOfComponents = println(ConfigMgr.getStepOfComponent(Set("002001", "002002")))
  
  def getNextStep = ConfigMgr.getNextStep(Set(new SelectedComponent("002001"), new SelectedComponent("002002")))
}