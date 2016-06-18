package org.configTree.component

import org.configTree.ConfigTree

/**
  * Created by gennadi on 29.04.16.
  */
abstract class Component extends ConfigTree {
  def id: String
  def nameToShow: String = ""
  def errorMessage: String = null
  def maxValue: Int = 0
  def minValue: Int = 0
  def value: Int = 0
  def defaultValue: Int = 0
  def interval: Int = 0 // 0 = default value
  def intervals: List[Int] = List.empty //specific intervals
}

case class ImmutableComponent(
                    id: String,
                    override val nameToShow: String
                    ) extends Component{
  require(id != "000000", "id must not be 000000")
  require(nameToShow != null, "nameToShow should not be null")
//  require(maxValue == 0, "maxValue should be 0")
//  require(minValue == 0, "minValue should be 0")
//  require(defaultValue == 0, "defaultValue should be 0")
//  require(interval == 0, "interval should be 0")
//  require(intervals == List.empty, "interval should be 0")
}
                    
case class MutableComponent(
                           id: String,
                           override val nameToShow: String,
                           override val minValue: Int,
                           override val maxValue: Int,
                           override val defaultValue: Int,
                           override val interval: Int,
                           override val intervals: List[Int]
                           ) extends Component {
  require(id != "000000", "id must not be 000000")
  require(nameToShow != null, "nameToShow should not be null")
//  require(maxValue != 0, "maxValue should not be 0")
//  require(minValue != 0, "minValue should not be 0")
//  require(defaultValue != 0, "defaultValue should not be 0")
//  require(interval != 0 && intervals == List.empty, "interval should not be 0")
//  require(interval == 0 && intervals != List.empty, "interval should not be 0")
}

case class SelectedComponent(
                           id: String,
                           override val value: Int = -1
                            ) extends Component


/**
 * id = 7
 */
case class ErrorComponent(
                         id: String, 
                         override val errorMessage: String
                         ) extends Component{
    require(id == "7", "id must be 7") 
}

case class SuccessComponent(id: String
    ) extends Component

case class CurrentConfigMutableComponent(
                                id: String,
                                override val nameToShow: String,
                                override val value: Int
                                ) extends Component

case class CurrentConfigImmutableComponent(
                                id: String,
                                override val nameToShow: String
                                ) extends Component
