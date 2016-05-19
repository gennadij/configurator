package org.configTree.component

import org.specs2.Specification
import org.configMgr.ConfigMgr

class MutableOrImmutableComponentSpec extends Specification{
  sequential
  def is = s2"""
    This Spec check mutable and Immutable objects
    
      Mutable                     $e1
      Immutable                   $e2
    
    """
  def e1 = {
    
    val imComponent = List(new ImmutableComponent("001001","component 001001"))
    
    ConfigMgr.startConfig.components filter (_.id == "001001") must_== imComponent
  }
  
  def e2 = {
    val imComponent = List(new MutableComponent("003003","component 003003"))
    
    ConfigMgr.getNextStep("002001").components filter (_.id == "003003") must_== imComponent
  }
}