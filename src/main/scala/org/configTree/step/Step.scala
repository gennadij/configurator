package org.configTree.step

import org.configTree._
import org.configTree.component.Component

/**
  * Created by gennadi on 29.04.16.
  */

//abstract class AbstractStep extends ConfigTree

abstract class AbstractStep extends ConfigTree {
  def id: String
  def nameToShow: String
  def nextStep: Seq[NextStep] = Seq.empty
  def selectionCriterium: SelectionCriterium = null
  def from: Source = null
  def components: Seq[Component] = null
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

case class FinalStep(id: String, nameToShow: String) extends AbstractStep {
  require(id == "000", "id must be 000")
}

case class NextStep     (
                              byComponent: String,
                              nextStep: String
                        )               
                        
case class ErrorStep(id: String, nameToShow: String) extends AbstractStep{
  require(id == "000", "id must be 000")
  require(!nameToShow.isEmpty(), "id must be not empty")
}