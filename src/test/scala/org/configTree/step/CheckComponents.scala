package org.configTree.step

import org.specs2.Specification
import org.configMgr.ConfigMgr

class CheckComponents extends Specification{
  def is = s2"""
    
    Spec for the Validity of Components with follow ErrorSteps
    
      The selected components has not been found in any configuration steps                 $e1
    
    """
  val errorE1 = new ErrorStep("7", "error step", "The selected components has " + 
            "not been found in any configuration steps")
  
  def e1 = ConfigMgr.getNextStep(Set("000000")) must_== errorE1 
  
}