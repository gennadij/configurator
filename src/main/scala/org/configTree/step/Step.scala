package org.configTree.step

import org.configTree._
import org.configTree.component._

/**
  * Created by gennadi on 29.04.16.
  * 
  * 
  */

//abstract class AbstractStep extends ConfigTree

abstract class AbstractStep extends ConfigTree {
  def id: String
  def nameToShow: String = ""
  //DefaultStep, FirstStep, LastStep
  def nextStep: Seq[NextStep] = Seq.empty
  def selectionCriterium: SelectionCriterium = null
  def from: Source = null
  def components: Seq[Component] = null
  //ErrorStep 
  def errorComponent: List[ErrorComponent] = Nil
  def errorMessage: List[String] = Nil
  //SaccessStep
  def succsessMessage: String = ""
  //NextStep
  def byComponent: String = ""
  def step: String = ""
}

case class DefaultStep  (
                              id: String,
                              override val nameToShow: String = "",
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium,
                              override val from: Source,
                              override val components: Seq[Component]
                        ) extends AbstractStep {
  require(id != "000", "id must not bi 000")
  require(id.size <= 3, "id size must be greater as 3")
  require(!nextStep.isEmpty, "nextStep must not be empty")
  require(selectionCriterium != null, "must not be null")
  require(from != null, "must not be null")
  require(!components.isEmpty, "components list should not be empty")
  require(errorMessage == "", "must be empty")
  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}
                        
case class FirstStep    (
                              id: String,
                              override val nameToShow: String = "",
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium = null,
                              override val from: Source,
                              override val components: Seq[Component]
                        ) extends AbstractStep {
  require(id != "000", "id must not bi 000")
  require(id.size <= 3, "id size must be greater as 3")
  require(!nextStep.isEmpty, "nextStep must not be empty")
  require(selectionCriterium != null, "must not be null")
  require(from != null, "must not be null")
  require(!components.isEmpty, "components list should not be empty")
  require(errorMessage == "", "must be empty")
  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}

case class LastStep     (
                              id: String,
                              override val nameToShow: String = "",
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium = null,
                              override val from: Source,
                              override val components: Seq[Component]
                        ) extends AbstractStep {
  require(id != "000", "id must not bi 000")
  require(id.size <= 3, "id size must be greater as 3")
  require(!nextStep.isEmpty, "nextStep must not be empty")
  require(selectionCriterium != null, "must not be null")
  require(from != null, "must not be null")
  require(!components.isEmpty, "components list should not be empty")
  require(errorMessage == "", "must be empty")
  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}

/**
 * Final step hat immer id 0
 */
case class FinalStep(id: String) extends AbstractStep {
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
                        ) extends AbstractStep{
  require(id == "1", "id must be 1")
  require(id.size == 1, "id size must be 1")
//  require(nextStep.isEmpty, "nextStep must be empty")
  require(selectionCriterium == null, "must be null")
  require(from == null, "must be null")
  require(components == null, "components list should be null")
  require(errorMessage == "", "must be empty")
  require(succsessMessage == "", "must be empty")
  require(byComponent != "", "must be not empty")
  require(step != "", "must be not empty")
}
/**
 * Error step hat immer id 7                        
 */
case class ErrorStep(         id: String,      // id for step within was error
                              override val errorComponent: List[ErrorComponent], // for error within component
                              override val errorMessage: List[String] // for error within step 
                    ) extends AbstractStep{
  require(id == "7", "id must be 7")
  require(id.size == 1, "id size must be 1")
//  require(!nameToShow.isEmpty(), "id must be not empty")
  require(nextStep.isEmpty, "nextStep must be empty")
  require(selectionCriterium == null, "must be null")
  require(from == null, "must be null")
  require(components == null, "components list should be empty")
  require(errorMessage != "", "must be not empty")
  require(succsessMessage == "", "must be empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}

case class SuccessStep(
                               id: String,
                               override val succsessMessage: String
                       ) extends AbstractStep {
  require(id == "3", "id must be 3")
  require(id.size == 1, "id size must be 1")
//  require(!nameToShow.isEmpty(), "id must be not empty")
  require(nextStep.isEmpty, "nextStep must be empty")
  require(selectionCriterium == null, "must be null")
  require(from == null, "must be null")
  require(components.isEmpty, "components list should be empty")
  require(errorMessage == "", "must be empty")
  require(succsessMessage != "", "must be not empty")
  require(byComponent == "", "must be empty")
  require(step == "", "must be empty")
}

case class CurrentConfigStep(
                               id: String,
                               override val nameToShow: String = "",
                               override val components: Seq[Component]
                               ) extends AbstractStep{
  
}



