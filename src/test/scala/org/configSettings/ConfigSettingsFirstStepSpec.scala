package org.configSettings

import org.specs2._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ConfigSettingsFirstStepSpec extends Specification {
  sequential 
  
  def is = s2"""

    "This is specification for leniar expiration from configurator from Step 1 to Step 7

    The Step consist of
      1. id=001                                                $e1
      2. nameToShow=step 001                                   $e2
      3. Size NextStep                                         $e3
      4. nextSteps->nextStep->component=001001                 $e4
      5. nextSteps->nextStep->step=002                         $e5
      6. nextSteps->nextStep->component=001002                 $e6
      7. nextSteps->nextStep->step=002                         $e7
      8. nextSteps->nextStep->component=001003                 $e8
      9. nextSteps->nextStep->step=003                         $e9
      11. selectionCriterium->min=1                            $e11
      12. selectionCriterium->max=1                            $e12
      13. components->from->source=xml                         $e13
      14. components->from->sqlStatement=                      $e14
      15. components->from->path=                              $e15
      16. components->component->id=001001                     $e16
      18. components->component->nameToShow=component=001001   $e18
      19. components->component->id=001002                     $e19
      21. components->component->nameToShow=component=001002   $e21
      22. components->component->id=001003                     $e22
      24. components->component->nameToShow=component=001003   $e24

                                                                """

  val staticStep = ConfigSettings.configSettings.configSettings(0)

  def e1 = staticStep.id must beEqualTo("001")

  def e2 = staticStep.nameToShow must beEqualTo("step 001")

  def e3 = staticStep.nextStep.size must_==3

  def e4 = staticStep.nextStep(0).byComponent must_== "001001"

  def e5 = staticStep.nextStep(0).step must_== "002"
  
  def e6 = staticStep.nextStep(1).byComponent must_== "001002"

  def e7 = staticStep.nextStep(1).step must_== "002"
  
  def e8 = staticStep.nextStep(2).byComponent must_== "001003"

  def e9 = staticStep.nextStep(2).step must_== "003"

  def e11 = staticStep.selectionCriterium.min must beEqualTo("1")

  def e12 = staticStep.selectionCriterium.max must beEqualTo("1")

  def e13= staticStep.from.source must beEqualTo("xml")

  def e14 = staticStep.from.sqlStatement must beEqualTo("")

  def e15 = staticStep.from.path must beEqualTo("")

  def e16 = staticStep.components(0).id must beEqualTo("001001")

  def e18 = staticStep.components(0).nameToShow must beEqualTo("component 001001")

  def e19 = staticStep.components(1).id must beEqualTo("001002")

  def e21 = staticStep.components(1).nameToShow must beEqualTo("component 001002")

  def e22 = staticStep.components(2).id must beEqualTo("001003")

  def e24 = staticStep.components(2).nameToShow must beEqualTo("component 001003")
}