package linearExpiratin

import org.specs2._
import configMgr._

class linearExpiratinSpec extends Specification { def is = s2"""

"This is specification for leniar expiration from configurator from Step 1 to Step 7

The first Step should
  Step 001 contain 3 Components                        $e1
  Selection Component 001003-> nextStepID=002          $e2
  Selection Component 002002-> nextStepID=003          $e3
  
                                              """
  
  val configMgr = new ConfigMgr
  val container = configMgr.loadStepsFromXML
  val currentConfig = configMgr.loadCurrentConfig
  val firstStep = configMgr.getFirstStep(container)
  
  def e1 = firstStep.components.length must_==3
  
  val selection1 = "001001"
  
  val nextStep1 = configMgr.getNextStep(container, selection1)
  
  
  def e2 = nextStep1._1.id must beEqualTo("002")
  
  val selection2 = "002002"
  
  val nextStep2 = configMgr.getNextStep(container, selection2)
  
  def e3 = nextStep2._1.id must beEqualTo("003")
  
  
  
}