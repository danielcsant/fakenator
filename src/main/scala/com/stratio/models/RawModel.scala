package com.stratio.models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Random

case class RawModel (order_id: String,
                     timestamp: String,
                     client_id: Int,
                     latitude: Double,
                     longitude: Double,
                     payment_method: String,
                     credit_card: String,
                     shopping_center: String,
                     employee: Int,
                     total_amount: Float) {}


object RawModel {

  val MaxLines = 30
  val Range_client_id = (1, 30000)
  val Range_payment_method = Source.fromInputStream(
  this.getClass.getClassLoader.getResourceAsStream("payment-methods.txt")).getLines().toSeq
  val Range_shopping_center = Source.fromInputStream(
    this.getClass.getClassLoader.getResourceAsStream("shopping-centers.txt")).getLines().toSeq
  val Range_employee = (1, 300)
  val Range_quantity = (1, 30)
  val R = Random

  val Range_family_product: Map[String, Map[String,Float]] = Source.fromInputStream(
    this.getClass.getClassLoader.getResourceAsStream("family-products.csv")).getLines().map(x => {
      val splitted = x.split(",")
      (splitted(0), Map(splitted(1) -> splitted(2).toFloat))
    }).toMap


  def generateShoppingCenter(): String = {
    Range_shopping_center(generateRandomInt(0, Range_shopping_center.length - 1))
  }

  def generatePaymentMethod(): String = {
    Range_payment_method(generateRandomInt(0, Range_payment_method.length - 1))
  }

  def generateTimestamp(): String = {
    val datetime = new DateTime().minusDays(generateRandomInt(0,60))
    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss ZZ").print(datetime)
  }

  def generateRandomInt(min: Int, max: Int): Int = {
    R.nextInt((max -min) + 1) + min
  }

  @tailrec
  def generateCreditCard(current: String): String = {
    if(current.length != 16) generateCreditCard(current + generateRandomInt(0,9))
    else current
  }
}