package org.main

import org.configMgr.ConfigMgr

class TestSelectionCriterium {
  
  def selectCriterium = ConfigMgr.getNextStep(List("001001", "001002"))
  
  def getStepOfComponents = println(ConfigMgr.getStepOfComponent(List("002001", "002002")))
  
  def getNextStep = ConfigMgr.getNextStep(List("002001", "002002"))
}