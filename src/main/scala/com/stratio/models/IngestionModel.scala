package com.stratio.models

import java.text.{SimpleDateFormat, DateFormat}
import java.util.{TimeZone, Calendar}

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Random
import org.json4s._
import org.json4s
import org.json4s.JsonDSL._
import org.json4s.jackson.Serialization._
import org.json4s.jackson.JsonMethods._

case class IngestionModel (order_id: String,
                     timestamp: String,
                     columns: Seq[ColumnModel]) {}

case class ColumnModel (column: String,
                      value: String)

object IngestionModel {

  val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
  val MORNING_THRESHOLD = 13;
  val AFTERNOON_THRESHOLD = 21;

  implicit val formats = DefaultFormats

  def getDayTimeZone(timestamp: String): String = {
    val df: DateFormat = new SimpleDateFormat(DATE_FORMAT);
    val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
    cal.setTime(df.parse(timestamp));
    val hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
    if (hourOfDay < MORNING_THRESHOLD)  {
      "morning"
    } else if (hourOfDay < AFTERNOON_THRESHOLD) {
      "afternoon"
    } else  {
      "noon"
    }
  }

  def generateColumns(clientId: Int,
                      timestamp: String,
                      latitude: Double,
                      longitude: Double,
                      paymentMethod: String,
                      creditCard: String,
                      shoppingCenter: String,
                      employee: Int,
                      totalAmount: Float,
                      lines: Seq[LineModel]): Seq[ColumnModel] = {

    val json = Extraction.decompose(lines)

    Seq(
      ColumnModel("datetime", timestamp),
      ColumnModel("order_id",""),
      ColumnModel("day_time_zone",getDayTimeZone(timestamp)),
      ColumnModel("client_id", clientId.toString),
      ColumnModel("payment_method", paymentMethod),
      ColumnModel("latitude", latitude.toString),
      ColumnModel("longitude", longitude.toString),
      ColumnModel("credit_card", creditCard),
      ColumnModel("shopping_center", shoppingCenter),
      ColumnModel("channel",""),
      ColumnModel("city",""),
      ColumnModel("country",""),
      ColumnModel("employee", employee.toString),
      ColumnModel("total_amount", totalAmount.toString),
      ColumnModel("total_products", lines.size.toString),
      ColumnModel("order_size", lines.size.toString),
      ColumnModel("lines", pretty(json).replaceAll("\n","").replaceAll(" ",""))


    )
  }

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

  def generateLines(): Seq[LineModel] = {
    (1 to generateRandomInt(1,MaxLines)).map(x => {
      val family = Range_family_product.keySet.toSeq(generateRandomInt(0, Range_family_product.keySet.size - 1))
      val product: String = Range_family_product.get(family)
        .get.keySet.toSeq(generateRandomInt(0, Range_family_product.get(family).get.keySet.size - 1))
      val price: Float = Range_family_product.get(family).get.get(product).get
      val quantity = generateRandomInt(1, 30)
      new LineModel(product, family, quantity, price)
    })
  }

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