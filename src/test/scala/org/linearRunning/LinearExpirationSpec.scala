package org.linearRunning

import org.specs2._
import org.configSettings.ConfigSettings

class LinearExpirationSpec extends Specification {
  def is = s2"""

    "This is specification for leniar expiration from configurator from Step 1 to Step 7

    The Step consist of
      1. id=001                                                $e1
      2. nameToShow=step 001                                   $e2
      3. nextSteps -> default=000                              $e3
      4. nextSteps->nextStep->component=000000                 $e4
      5. nextSteps->nextStep->step=000                         $e5
      6. kind=first                                            $e6
      7. selectionCriterium->min=1                             $e7
      8. selectionCriterium->max=1                             $e8
      9. components->from->source=xml                          $e9
      10. components->from->sqlStatement=                      $e10
      11. components->from->path=                              $e11
      12. components->component->id=001001                     $e12
      13. components->component->kind=immutable                $e13
      14. components->component->nameToShow=component=001001   $e14
      15. components->component->id=001002                     $e15
      16. components->component->kind=immutable                $e16
      17. components->component->nameToShow=component=001002   $e17
      18. components->component->id=001003                     $e18
      19. components->component->kind=immutable                $e19
      20. components->component->nameToShow=component=001003   $e20

                                                                """

  val staticStep = ConfigSettings.configSettingsV01.configSettingsForStatic(0)

  def e1 = staticStep.id must beEqualTo("001")

  def e2 = staticStep.nameToShow must beEqualTo("step 001")

  def e3 = staticStep.nextStep(0).default must beEqualTo("000")

  def e4 = staticStep.nextStep(0).byComponent must beEqualTo("000000")

  def e5 = staticStep.nextStep(0).nextStep must beEqualTo("000")

  def e6 = staticStep.kind must beEqualTo("first")

  def e7 = staticStep.selectionCriterium.min must beEqualTo("1")

  def e8 = staticStep.selectionCriterium.max must beEqualTo("1")

  def e9 = staticStep.from.source must beEqualTo("xml")

  def e10 = staticStep.from.sqlStatement must beEqualTo("")

  def e11 = staticStep.from.path must beEqualTo("")

  def e12 = staticStep.components(0).id must beEqualTo("001001")

  def e13 = staticStep.components(0).kind must beEqualTo("immutable")

  def e14 = staticStep.components(0).nameToShow must beEqualTo("component 001001")

  def e15 = staticStep.components(1).id must beEqualTo("001002")

  def e16 = staticStep.components(1).kind must beEqualTo("immutable")

  def e17 = staticStep.components(1).nameToShow must beEqualTo("component 001002")

  def e18 = staticStep.components(2).id must beEqualTo("001003")

  def e19 = staticStep.components(2).kind must beEqualTo("immutable")

  def e20 = staticStep.components(2).nameToShow must beEqualTo("component 001003")
}