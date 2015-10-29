package com.stratio.models

import scala.slick.driver.MySQLDriver.simple._

class RawModels(tag: Tag) extends Table[(String, String, Int, Double, Double, String, String, String, Int, Float   )](tag, "RAW_MODELS") {

  def order_id = column[String]("ORDER_ID", O.PrimaryKey)
  def timestamp = column[String]("TIMESTAMP")
  def client_id = column[Int]("CLIENT_ID")
  def latitude = column[Double]("LATITUDE")
  def longitude = column[Double]("LONGITUDE")
  def payment_method = column[String]("PAYMENT_METHOD")
  def credit_card = column[String]("CREDIT_CARD")
  def shopping_center = column[String]("SHOPPING_CENTER")
  def employee = column[Int]("EMPLOYEE")
  def total_amount = column[Float]("TOTAL_AMOUNT")

  def * = (order_id, timestamp, client_id, latitude, longitude, payment_method, credit_card, shopping_center, employee, total_amount)
}