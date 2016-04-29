package org.configTree

/**
  * Created by gennadi on 29.04.16.
  */
// TODO nestStepp: Boolean
case class Step (
               id: String,
               nameToShow: String,
               nextStep: String,
               isStartStep: String,
               components: Seq[ImmutableComponent]
               ) extends ConfigTree
