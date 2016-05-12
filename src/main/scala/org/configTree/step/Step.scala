package org.configTree.staticStep

import org.configTree._
import org.configTree.staticStep._
import org.configTree.component.StaticComponent
import org.configTree.component.Component
import org.configTree.step.Source
import org.configTree.step.SelectionCriterium

/**
  * Created by gennadi on 29.04.16.
  */

abstract class AbstractStep extends ConfigTree

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
                )extends AbstractStep

                   //case class StaticStep(
//                       id: String,
//                       nameToShow: String,
//                       nextStep: Seq[NextStep],
//                       kind: String,
//                       selectionCriterium: SelectionCriterium = null,
//                       from: Source,
//                       components: Seq[StaticComponent]
//                     ) extends AbstractStep