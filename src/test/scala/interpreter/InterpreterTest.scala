package interpreter

import org.scalatest.FunSuite

class InterpreterTest extends FunSuite {
  test("interpret 1+2") {
    val interpreter = new Interpreter("1+2")
    assert(interpreter.expr() == 3)
  }
}
