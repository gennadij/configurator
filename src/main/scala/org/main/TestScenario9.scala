package org.main

import org.admin.Admin
import org.configTree.step.Step
import org.configTree.step.DefaultStep

class TestScenario9 {
  
  
  def scenario9 = {
    
    val step = new DefaultStep("S000001", "nameToShow", "", Nil, null, null, Nil, Nil)
    Admin.setStep("config1", true, step)
  }
  
  
}