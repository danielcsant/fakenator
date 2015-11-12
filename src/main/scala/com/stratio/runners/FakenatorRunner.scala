package com.stratio.runners

import java.lang.Double
import java.util.UUID

import com.stratio.decision.api.StratioStreamingAPIFactory
import com.stratio.decision.api.messaging.{ColumnNameValue, ColumnNameType}
import com.stratio.decision.commons.constants.ColumnType
import com.stratio.decision.commons.exceptions.StratioStreamingException
import scala.collection.JavaConversions._

object FakenatorRunner {

  val stratioStreamingAPI = StratioStreamingAPIFactory
    .create
    .withServerConfig("ip-172-31-3-208",9092, "ip-172-31-3-208", 2181)
    .init

  val streamName = "chustas"

  println(streamName)

  def main(args: Array[String]): Unit = {
    if(args.size == 0) {
      println(s">> Creating stream: $streamName")
      createStream
    }

    insertData
  }

  def createStream(): Unit = {
    val name = new ColumnNameType("name", ColumnType.STRING)
    val data1 = new ColumnNameType("data1", ColumnType.INTEGER)
    val data2 = new ColumnNameType("data2", ColumnType.FLOAT)

    val columnList = Seq(name, data1, data2)

    try {
      stratioStreamingAPI.createStream(streamName, columnList)
      stratioStreamingAPI.saveToMongo(streamName)
      stratioStreamingAPI.listenStream(streamName)

      UUID.randomUUID()
    } catch {
      case ssEx: StratioStreamingException => println(ssEx.printStackTrace())
    }
  }



  def insertData(): Unit = {
    var firstColumnValue = new ColumnNameValue("name", "test")
    var secondColumnValue = new ColumnNameValue("data1", new Integer(1))
    var thirdColumnValue = new ColumnNameValue("data2", new Double(2))
    var streamData = Seq(firstColumnValue, secondColumnValue, thirdColumnValue)

    try {
      var i: Integer = 1

      while(true) {
        println(s">> Inserting element: $i")
        firstColumnValue = new ColumnNameValue("name", s"test-$i")
        secondColumnValue = new ColumnNameValue("data1", new Integer(i))
        thirdColumnValue = new ColumnNameValue("data2", new Double(i.toDouble))
        streamData = Seq(firstColumnValue, secondColumnValue, thirdColumnValue)

        stratioStreamingAPI.insertData(streamName, streamData)

        if(i % 10 == 0) {
          i = 1
          Thread.sleep(500)
        } else {
          i = i + 1
        }
      }
    } catch {
      case ssEx: StratioStreamingException => println(ssEx.printStackTrace())
    }
  }
}
