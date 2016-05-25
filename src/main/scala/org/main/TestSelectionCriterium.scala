package org.main

import org.configMgr.ConfigMgr

class TestSelectionCriterium {
  
  def selectCriterium = ConfigMgr.getNextStep(Set("001001", "001002"))
  
//  def getStepOfComponents = println(ConfigMgr.getStepOfComponent(Set("002001", "002002")))
  
  def getNextStep = ConfigMgr.getNextStep(Set("002001", "002002"))
}