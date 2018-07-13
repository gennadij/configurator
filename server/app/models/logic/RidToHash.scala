package models.logic

import play.api.Logger

import scala.collection.mutable

/**
 * Copyright (C) 2016 Gennadi Heimann genaheimann@gmail.com
 * 
 * Created by Gennadi Heimann 25.05.2018
 */
object RidToHash {
  
  
  var idHash: mutable.Map[String, String] = scala.collection.mutable.Map[String, String]()
  
  
  def setIdAndHash(id: String): (String, String) = if (idHash.exists(_._1 == id)) {
//    idHash.foreach(item => Logger.info("Item" + item.toString))
    (id, calculateHash(id))
  } else {
    val hash = calculateHash(id)
    idHash += (id -> hash)
//    idHash.foreach(item => Logger.info("Item" + item.toString))
    (id, hash)
  }
  
  def getRId(hash: String): Option[String] = {
    val item = idHash.find(_._2 == hash )
    item match {
      case Some(i) => Some(i._1)
      case None => None
    }


  }
  
  def getHash(id: String): Option[String] = {
    val item = idHash.find(_._1 == id)
    item match {
      case Some(i) => Some(i._2)
      case None => None
    }
  }
  
  def cleanMap: mutable.Map[String, String] = {
    idHash.empty
  }
  
  def calculateHash(id: String): String = {
    import java.math.BigInteger
    import java.security.MessageDigest
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(id.getBytes)
    val bigInt = new BigInteger(1, digest)
    bigInt.toString(16)
  }

  def printHashes: Unit = {
    idHash foreach(iH => Logger.info("id: " + iH._1 + " -> " + "hash: " + iH._2))
  }
}