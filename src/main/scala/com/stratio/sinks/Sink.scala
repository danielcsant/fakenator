package com.stratio.sinks

/**
 * Created by dcarroza on 23/11/15.
 */
trait Sink {

  def write(data: String): Unit

}
