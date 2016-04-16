package configMgr

import org.json4s._
import org.json4s.jackson.JsonMethods._
object Main {
  def main(args : Array[String]) = {
    println("I am configurator")

    //TODO create Factory object
    val configMgr = new ConfigMgr

    for(step <- configMgr.getSteps) {
      println("Die Steps: " + step)
    }

    for(component <- configMgr.getComponentsForStep("001")){
      println("Die Components fuer Id = 001: " + component)
    }
  }
}