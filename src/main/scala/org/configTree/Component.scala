package org.configTree

/**
  * Created by gennadi on 29.04.16.
  */
abstract class Component extends ConfigTree {
  def nextStep: String
}
