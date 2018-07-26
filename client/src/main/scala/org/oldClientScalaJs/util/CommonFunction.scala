package org.oldClientScalaJs.util

import org.oldClientScalaJs.wrapper.Status


/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 05.03.2018
 */
object CommonFunction {
  
  def getStatus(status: Option[Status]): String = {
    new CommonFunction().getStatus(status)
  }
}

class CommonFunction {
  
  
  private def getStatus(status: Option[Status]): String = status match {
    case Some(s) => s.status
    case None => "None"
  }
}