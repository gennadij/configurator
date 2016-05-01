package org.linearExpiratoin

import org.specs2._
import main._
import org.configMgr.ConfigMgr
import org.configSettings.ConfigSettings

class LinearExpirationSpec extends Specification {def is = s2"""

"This is specification for leniar expiration from configurator from Step 1 to Step 7

The first Step should
  Step 001 contain 3 Components                         $e1
  Currentconfig size = 0                                $e2
  Selection Component 001001 -> nextStepID=002          $e3
  CurrentConfig size = 1                                $e4
  CurrentConfig StepID = 001                            $e5
  CurrentConfig ComponentID = 001001                    $e6
  Selection Component 002002 -> nextStepID=003          $e7
  Selection Component 003003 -> nextStepID=004          $e8
  Selection Component 004003 -> nextStepID=006          $e9
  CurrentConfig size = 8                                $e10

                                              """

  val configMgr = new ConfigMgr
  val container = ConfigSettings.configSettings
  val firstStep = configMgr.getFirstStep(container)

  def e1 = firstStep.components.length must_== 3

  def e2 = container.currentConfig.size must_==4

  val selection1 = "001001"

  val nextStep1 = configMgr.getNextStep(container, selection1)

  def e3 = nextStep1._1.id must beEqualTo("002")

  def e4 = container.currentConfig.size must_==4

  def e5 = container.currentConfig(0).id must beEqualTo("001")

  def e6 = container.currentConfig(0).components(0).id must beEqualTo("001001")

  val selection2 = "002002"

  val nextStep2 = configMgr.getNextStep(container, selection2)

  def e7 = nextStep2._1.id must beEqualTo("003")

  val selection3 = "003003"

  val nextStep3 = configMgr.getNextStep(container, selection3)

  def e8 = nextStep3._1.id must beEqualTo("004")

  val selection4 = "004003"

  val nextStep4 = configMgr.getNextStep(container, selection4)

  def e9 = nextStep4._1.id must beEqualTo("006")



  def e10 = container.currentConfig.size must beEqualTo(8)

  def printCurrentConfig = {
    println("Aktuelle Konfiguration")
    for(step <- container.currentConfig){
      println(step.nameToShow)
      for(comp <- step.components){
        println("Components: " + comp.nameToShow)
      }
    }
  }
}