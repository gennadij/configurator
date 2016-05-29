package org.configTree.component

import org.configTree.ConfigTree

/**
  * Created by gennadi on 29.04.16.
  */
abstract class Component extends ConfigTree {
  def id: String
  def nameToShow: String
  def errorMessage: String = null
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

