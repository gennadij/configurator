package org

import org.specs2.Specification
import org.configMgr.ConfigMgr

/**
  * Created by gennadi on 12.05.16.
  */
class RunConfig extends Specification{ def is =
  s2"""
      Specification for whole Config by step 001, 003, 004, 006
        Step 001                        $e1
        Step 003                        $e2
        step 004                        $e3
        step 006                        $e4
    """

  val configMgr = new ConfigMgr
  def e1 = configMgr.startConfigV01.id must_== "001"
  def e2 = configMgr.getNextStepV01("001003").id must_== "003"
  def e3 = configMgr.getNextStepV01("003002").id must_== "004"
  def e4 = configMgr.getNextStepV01("004003").id must_== "006"

}
