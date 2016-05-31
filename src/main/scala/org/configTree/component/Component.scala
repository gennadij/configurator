package org.configTree.component

import org.configTree.ConfigTree

/**
  * Created by gennadi on 29.04.16.
  */
abstract class Component extends ConfigTree {
  def id: String
  def nameToShow: String
  def errorMessage: String = null
  def maxValue: Int = 0
  def minValue: Int = 0
  def defaultValue: Int = 0
  def interval: Int = 0 // 0 = default value
  def intervals: List[Int] = List.empty //specific intervals
}

case class ImmutableComponent(
                    id: String,
                    nameToShow: String
                    ) extends Component{
  require(id != "000000", "id must not be 000000")
}
                    
case class MutableComponent(
                           id: String,
                           nameToShow: String
                           ) extends Component {
  require(id != "000000", "id must not be 000000")
}
/**
 * id = 7
 */
case class ErrorComponent(id: String,
                           nameToShow: String,
                           override val errorMessage: String) extends Component{
    require(id == "7", "id must be 7") 
}

