package org.linearExpiratoin

import org.specs2._
import main._
import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings

class LinearExpirationSpec extends Specification { def is = s2"""

"This is specification for leniar expiration from configurator from Step 1 to Step 7

The first Step should
  Step 001 contain 3 Components                        $e1
  Selection Component 001001 -> nextStepID=002          $e2
  Selection Component 002002 -> nextStepID=003          $e3
  Selection Component 003003 -> nextStepID=004          $e4
  Selection Component 004003 -> nextStepID=006          $e5
  
                                              """
  
  val configMgr = new ConfigMgr
  val container = ConfigSettings.configSettings
  val currentConfig = configMgr.loadCurrentConfig
  val firstStep = configMgr.getFirstStep(container)
  
  def e1 = firstStep.components.length must_==3
  
  val selection1 = "001001"
  
  val nextStep1 = configMgr.getNextStep(container, selection1)
  
  
  def e2 = nextStep1._1.id must beEqualTo("002")
  
  val selection2 = "002002"
  
  val nextStep2 = configMgr.getNextStep(container, selection2)
  
  def e3 = nextStep2._1.id must beEqualTo("003")

  val selection3 = "003003"

  val nextStep3 = configMgr.getNextStep(container, selection3)

  def e4 = nextStep3._1.id must beEqualTo("004")

  val selection4 = "004003"

  val nextStep4 = configMgr.getNextStep(container, selection4)

  def e5 = nextStep4._1.id must beEqualTo("006")

}