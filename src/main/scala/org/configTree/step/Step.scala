package org.configTree.step

import org.configTree._
import org.configTree.component.Component

/**
  * Created by gennadi on 29.04.16.
  * 
  * 
  */

//abstract class AbstractStep extends ConfigTree

abstract class AbstractStep extends ConfigTree {
  def id: String
  def nameToShow: String
  //DefaultStep, FirstStep, LastStep
  def nextStep: Seq[NextStep] = Seq.empty
  def selectionCriterium: SelectionCriterium = null
  def from: Source = null
  def components: Seq[Component] = null
  //ErrorStep 
  def errorMessage: String = ""
  //SaccessStep
  def succsessMessage: String = ""
  //NextStep
  def byComponent: String = ""
  def step: String = ""
}

case class DefaultStep  (
                              id: String,
                              nameToShow: String,
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium,
                              override val from: Source,
                              override val components: Seq[Component]
                        ) extends AbstractStep {
  require(id != "000", "id must not bi 000")
  require(!components.isEmpty, "components list should not be empty")
}
                        
case class FirstStep    (
                              id: String,
                              nameToShow: String,
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium = null,
                              override val from: Source,
                              override val components: Seq[Component]
                        ) extends AbstractStep {
  require(id != "000", "id must not bi 000")
  require(!components.isEmpty, "components list should not be empty")
}

case class LastStep     (
                              id: String,
                              nameToShow: String,
                              override val nextStep: Seq[NextStep],
                              override val selectionCriterium: SelectionCriterium = null,
                              override val from: Source,
                              override val components: Seq[Component]
                        ) extends AbstractStep {
  require(id != "000", "id must not bi 000")
  require(!components.isEmpty, "components list should not be empty")
}

/**
 * Final step hat immer id 0
 */
case class FinalStep(id: String, nameToShow: String) extends AbstractStep {
  require(id == "0", "id must be 000")
}

/**
 * NextStep hat immer id 1
 */
case class NextStep     (
                              id: String,
                              nameToShow: String,
                              override val byComponent: String,
                              override val step: String
                        ) extends AbstractStep{
  require(id == "1", "id must bi 1")
}
/**
 * Error step hat immer id 7                        
 */
case class ErrorStep(         id: String, 
                              nameToShow: String, 
                              override val errorMessage: String
                    ) extends AbstractStep{
  require(id == "7", "id must be 0")
  require(!nameToShow.isEmpty(), "id must be not empty")
}

case class SuccessStep(
                               id: String,
                               nameToShow: String,
                               override val succsessMessage: String
                       ) extends AbstractStep {
  require(id == "3", "id must be 0")
  require(!nameToShow.isEmpty(), "id must be not empty")
}



