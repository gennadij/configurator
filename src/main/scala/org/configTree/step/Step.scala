package org.configTree.step

import org.configTree._
import org.configTree.component.Component
import org.configTree.component.StaticComponent

/**
  * Created by gennadi on 29.04.16.
  */

//abstract class AbstractStep extends ConfigTree

abstract class AbstractStep extends ConfigTree {
  def id: String
  def nameToShow: String
  def nextStep: Seq[NextStep]
  def kind: String
  def selectionCriterium: SelectionCriterium
  def from: Source
  def components: Seq[StaticComponent]
}

case class DefaultStep  (
                              id: String,
                              nameToShow: String,
                              nextStep: Seq[NextStep],
                              kind: String,
                              selectionCriterium: SelectionCriterium,
                              from: Source,
                              components: Seq[StaticComponent]
                        ) extends AbstractStep
                        
case class FirstStep    (
                              id: String,
                              nameToShow: String,
                              nextStep: Seq[NextStep],
                              kind: String,
                              selectionCriterium: SelectionCriterium = null,
                              from: Source,
                              components: Seq[StaticComponent]
                        ) extends AbstractStep

case class LastStep     (
                              id: String,
                              nameToShow: String,
                              nextStep: Seq[NextStep],
                              kind: String,
                              selectionCriterium: SelectionCriterium = null,
                              from: Source,
                              components: Seq[StaticComponent]
                        ) extends AbstractStep

case class NextStep     (
                              byComponent: String,
                              nextStep: String
                        )               
                        
                        

// TODO nextStep: Boolean
case class Step (
                  id: String,
                  nameToShow: String,
                  nextStep: String,
                  isStartStep: String,
                  components: Seq[Component]
                )

                   //case class StaticStep(
//                       id: String,
//                       nameToShow: String,
//                       nextStep: Seq[NextStep],
//                       kind: String,
//                       selectionCriterium: SelectionCriterium = null,
//                       from: Source,
//                       components: Seq[StaticComponent]
//                     ) extends AbstractStep