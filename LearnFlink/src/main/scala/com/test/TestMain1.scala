package com.test

import scala.util.parsing.combinator.PackratParsers
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

class TestMain1 {

}

object TestMain1 extends StandardTokenParsers with PackratParsers{
  lexical.delimiters ++= List(".",";","+","-","*")
  //合法的输入模式，支持加，减，乘
  lazy val pgm : PackratParser[Int] = expr | minus|multiply
  //定义模式加
  lazy val expr :PackratParser[Int]= num~"+"~num ^^ {case n1~"+"~n2 => n1.toInt + n2.toInt}
  //定义模式减
  lazy val minus :PackratParser[Int]= num~"-"~num ^^ {case n1~"-"~n2 => n1.toInt - n2.toInt}
  // 定义模式乘
  lazy val multiply :PackratParser[Int]= num~"*"~num ^^ {case n1~"*"~n2 => n1.toInt * n2.toInt}
  lazy val num = numericLit

  def parse(input: String) =
    phrase(pgm)(new PackratReader(new lexical.Scanner(input))) match {
      case Success(result, _) => println("Success!"); println(result);Some(result)
      case n @ _ => println(n);println("bla"); None
    }

  def main(args: Array[String]) {
    //定义list，::表示添加,Nil表示list结束
    val prg = "12*2"::"24-4"::"3+5"::Nil
    prg.map(parse)
  }
}
