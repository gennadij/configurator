package org.persistence.db.orientdb

import scala.collection.JavaConversions._

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertexType
import org.status.SuccessfulStatus
import org.status.WarningStatus
import com.orientechnologies.orient.core.metadata.schema.OType
import org.status.Status
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import scala.collection.immutable.Iterable

/**
 * Created by Gennadi Heimann 23.12.2016
 */

object AdminUserVertex {

  /**
   * detect configId from ConfigUri
   * 
   * @author Gennadi Heimann
   * 
   * @version 1.0
   * 
   * @param configUri
   * 
   * @return configId
   */
  def getConfigId(configUri: String): String = {
    val graph: OrientGraph = OrientDB.getGraph()
    val adminUser = PropertyKey.ADMIN_USER
    val resConfigId: OrientDynaElementIterable = graph
      .command(new OCommandSQL(s"select from $adminUser where configUri='$configUri'")).execute()
    
    val configIds = resConfigId.toList.map(_.asInstanceOf[OrientVertex].getIdentity)
    
    if(configIds.size == 1) configIds(0).toString() else ""
  }

}
