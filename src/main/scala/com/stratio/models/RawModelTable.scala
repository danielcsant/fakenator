package com.stratio.models

import scala.slick.driver.MySQLDriver.simple._

class RawModels(tag: Tag) extends Table[(String, String, Int, Double, Double, String, String, String, Int, Float   )](tag, "orders") {

  def order_id = column[String]("order_id", O.PrimaryKey)
  def timestamp = column[String]("timestamp")
  def client_id = column[Int]("client_id")
  def latitude = column[Double]("latitude")
  def longitude = column[Double]("longitude")
  def payment_method = column[String]("payment_method")
  def credit_card = column[String]("credit_card")
  def shopping_center = column[String]("shopping_center")
  def employee = column[Int]("employee")
  def total_amount = column[Float]("total_amount")

  def * = (order_id, timestamp, client_id, latitude, longitude, payment_method, credit_card, shopping_center, employee, total_amount)
}