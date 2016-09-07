package org.configTree.step

import org.configTree._
import org.configTree.component._

/**
  * Created by gennadi on 29.04.16.
  * 
  * ID for step => S000000 - S999999
  * ID for component => S000000C000000 - S999999C999999
  * 
  */


abstract class Step extends ConfigTree {
  val id: String
  def nameToShow: String = ""
  def fatherStep: String = ""
//  DefaultStep, FirstStep, LastStep
  def nextStep: Seq[NextStep] = Seq.empty
  def selectionCriterium: SelectionCriterium = null
  def from: Source = null
  def components: Seq[Component] = null
  //ErrorStep 
  def errorComponent: Seq[Component] = Nil
  def errorMessage: String = ""
//  SaccessStep
  def succsessMessage: String = ""
  //NextStep
  def byComponent: String = ""
  def step: String = ""
  def dependencies: List[Dependency] = null
}

abstract class ConfigSettingsStep extends Step {
  override def nameToShow: String = ""
  override def fatherStep: String = ""
  override def nextStep: Seq[NextStep] = Seq.empty
  override def selectionCriterium: SelectionCriterium = null
  override def from: Source = null
  override def components: Seq[Component] = null
  //NextStep
  override def byComponent: String = ""
  override def step: String = ""
  override def dependencies: List[Dependency] = null
}

abstract class AnnounceStep extends Step{
  //  //ErrorStep 
  override def errorComponent: Seq[Component] = Nil
  override def errorMessage: String = ""
    //SaccessStep
  override def succsessMessage: String = ""
}
	


case class DefaultStep  (
                              id: String,
                              override val nameToShow: String,
                              override val fatherStep: String,
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium,
                              override val from: Source,
                              override val components: Seq[Component],
                              override val dependencies: List[Dependency]
                        ) extends ConfigSettingsStep {
  require(id != "000", "id must not bi 000")
  require(id.size <= 7, "id size must be greater as 3")
  require(!nextStep.isEmpty, "nextStep must not be empty")
  require(selectionCriterium != null, "must not be null")
  require(from != null, "must not be null")
  require(!components.isEmpty, "components list should not be empty")
//  require(errorMessage == "", "must be empty")
//  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}
                        
case class FirstStep    (
                              id: String,
                              override val nameToShow: String = "",
                              override val fatherStep: String = "",
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium = null,
                              override val from: Source,
                              override val components: Seq[Component],
                              override val dependencies: List[Dependency]
                        ) extends ConfigSettingsStep {
  require(id != "000", "id must not bi 000")
  require(id.size <= 7, "id size must be greater as ")
  require(!nextStep.isEmpty, "nextStep must not be empty")
  require(selectionCriterium != null, "must not be null")
  require(from != null, "must not be null")
  require(!components.isEmpty, "components list should not be empty")
//  require(errorMessage == "", "must be empty")
//  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}

case class LastStep     (
                              id: String,
                              override val nameToShow: String = "",
                              override val fatherStep: String = "",
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium = null,
                              override val from: Source,
                              override val components: Seq[Component],
                              override val dependencies: List[Dependency]
                        ) extends ConfigSettingsStep {
  require(id != "000", "id must not bi 000")
  require(id.size <= 7, "id size must be greater as 3")
  require(nextStep.isEmpty, "nextStep must be empty")
  require(selectionCriterium != null, "must not be null")
  require(from != null, "must not be null")
  require(!components.isEmpty, "components list should not be empty")
//  require(errorMessage == "", "must be empty")
//  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}

/**
 * Final step hat immer id 0
 * 
 * FinalStep definiert das Ende vom Konfigurationsbaum
 * FinalStep hat keine Parameter
 * FinalStep ID = FS000000
 */
case class FinalStep(id: String) extends ConfigSettingsStep {
//  require(id == "0", "id must be 0")
//  require(id.size == 1, "id size must be 1")
////  require(nextStep.isEmpty, "nextStep must be empty")
//  require(selectionCriterium == null, "must be null")
//  require(from == null, "must be null")
//  require(components == null, "components list should be empty")
//  require(errorMessage == "", "must be empty")
//  require(succsessMessage == "", "must be empty")
//  require(byComponent == "", "must be empty")
////  require(step == "", "must be empty")
}

/**
 * NextStep hat immer id 1
 */
case class NextStep     (
                              id: String,
                              override val byComponent: String,
                              override val step: String
                        ) extends ConfigSettingsStep{
  require(id == "1", "id must be 1")
  require(id.size == 1, "id size must be 1")
//  require(nextStep.isEmpty, "nextStep must be empty")
  require(selectionCriterium == null, "must be null")
  require(from == null, "must be null")
  require(components == null, "components list should be null")
//  require(errorMessage == "", "must be empty")
//  require(succsessMessage == "", "must be empty")
  require(byComponent != "", "must be not empty")
  require(step != "", "must be not empty")
}
/**
 * Error step hat immer id 7                        
 */
case class ErrorStep(         id: String,      // id for step within was error
                              override val errorMessage: String, // for error within step 
                              override val errorComponent: Seq[Component] // for error within component
                    ) extends AnnounceStep{
//  require(id == "7", "id must be 7")
//  require(id.size == 1, "id size must be 1")
//  require(!nameToShow.isEmpty(), "id must be not empty")
//  require(nextStep.isEmpty, "nextStep must be empty")
//  require(selectionCriterium == null, "must be null")
//  require(from == null, "must be null")
//  require(components == null, "components list should be empty")
//  require(errorMessage != "", "must be not empty")
//  require(succsessMessage == "", "must be empty")
//  require(byComponent == "", "must be empty")
//  require(step == "", "must be empty")
}

case class SuccessStep(
                               id: String,
                               override val succsessMessage: String
                       ) extends AnnounceStep {
  require(id == "3", "id must be 3")
  require(id.size == 1, "id size must be 1")
//  require(!nameToShow.isEmpty(), "id must be not empty")
//  require(nextStep.isEmpty, "nextStep must be empty")
//  require(selctionCriterium == null, "must be null")
//  require(from == null, "must be null")
//  require(components.isEmpty, "components list should be empty")
//  require(errorMessage == "", "must be empty")
//  require(succsessMessage != "", "must be not empty")
//  require(byComponent == "", "must be empty")
//  require(step == "", "must be empty")
}

case class CurrentConfigStep(
                               id: String,
                               override val nameToShow: String = "",
                               override val components: Seq[Component]
                               ) extends ConfigSettingsStep{
  
}



