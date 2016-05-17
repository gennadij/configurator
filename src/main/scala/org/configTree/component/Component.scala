package org.configTree.component

import org.configTree.ConfigTree

/**
  * Created by gennadi on 29.04.16.
  */
abstract class Component extends ConfigTree {
  def id: String
  def kind: String
  def nameToShow: String
}

case class ImmutableComponent(
                    id: String,
                    kind: String,
                    nameToShow: String
                    ) extends Component
                    
case class MutableComponent(
                           id: String,
                           kind: String,
                           nameToShow: String
                           ) extends Component
