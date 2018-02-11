package interpreter

import scala.collection.mutable.ListBuffer

abstract class Token() {

}

case class EOF() extends Token

case class Plus() extends Token

case class Integer(value: Int) extends Token


class Interpreter(val text: String) {
  private var pos: Int = 0
  private var currentToken: Token = _

  private def readToken(): Token = {
    text.charAt(pos) match {
      case s if pos > text.length - 1 =>
        pos += 1
        EOF()
      case s if s.isDigit =>
        pos += 1
        Integer(s.asDigit)
      case s if s.equals('+') =>
        pos += 1
        Plus()
    }
  }

  private def eat(): Unit = {
    currentToken match {
      case Integer(_) if pos == 0 =>
        currentToken = readToken()
      case Plus() if pos > 0 => currentToken = readToken()
      case Integer(_) => currentToken = readToken()
      case _ => throw new Error("...!")
    }
  }

  def expr(): Int = {
    currentToken = readToken()

    val left = currentToken
    val lVal = left match {
      case Integer(n) => n
      case _ => throw new Error("integer plz")
    }
    eat()

    val op = currentToken

    val middle = currentToken match {
      case Plus() => Plus()
      case _ => throw new Error("+! plz")
    }

    eat()
    val right = currentToken
    val rVal = right match {
      case Integer(n) => n
      case _ => throw new Error("integer plz")
    }

    lVal + rVal
  }
}