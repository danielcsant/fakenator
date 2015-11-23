package com.stratio.sinks

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
/**
 * Created by dcarroza on 23/11/15.
 */
case class KafkaSink(topic: String) extends Sink {

  override def write(data: String): Unit = {

    val message: KeyedMessage[String, String] = new KeyedMessage[String, String](topic,data)

    producer.send(message)

  }

  val producer = getProducer

  def getProducer = {
    val props: Properties = new Properties();

    //    props.put("metadata.broker.list", "broker1:9092,broker2:9092")
    props.put("metadata.broker.list", "54.169.226.120:9092")
    props.put("serializer.class", "kafka.serializer.StringEncoder")
//    props.put("partitioner.class", "example.producer.SimplePartitioner")
    props.put("request.required.acks", "1")

    val config: ProducerConfig = new ProducerConfig(props)

    new Producer[String, String](config);
  }

}

