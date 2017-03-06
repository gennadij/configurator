package org.persistence

object GlobalConfigForDB {
  val activeDB: String = "test"
  val activeUrl = "http://contig/user12"
  //Test
  def db: DB = {
    activeDB match {
      case "test" => new TestDB
    }
  }
}

abstract class DB {
  val dbName: String
  val username: String
  val password: String
}

class TestDB extends DB {
  override val dbName = "testDB"
  override val username = "root"
  override val password = "root"
}