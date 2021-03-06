package fr.xebia.xke.fp5.exercise1

/**
  * Exercise de motivation pour le typeclass pattern. L'idée est d'implementer une fonctionne qui calcule la moyenne d'une liste
  * d'entiers, en suite d'une liste de double, et en suite d'une liste de numéros complexes
  */
object OopAverage {

  /*
  Implémenter une fonction qui calcule l'average d'une liste d'entiers
   */
  def avgInt(numbers: Int*): Int = numbers.reduce(_ + _) / numbers.length

  /*
  Implémenter une fonction qui calcule l'average d'une liste d'entiers
   */
  def avgDouble(numbers: Double*): Double = numbers.reduce(_ + _) / numbers.length


  /**
    * Q: quelle est la différence entre les fonctions avgInt et avgDouble?
    *
    * Q: Peut-on faire une fonction générique qui traite les Int et les Double à la fois? Dit
    * autrement, peut-on mutualiser le code des deux fonctions?
    * R: Oui, on peut, en utilisant par example le pattern Wrapper/Adapter
    *
    */
  sealed trait NumberAdapter {
    def +(n: NumberAdapter): NumberAdapter

    def /(n: NumberAdapter): NumberAdapter
  }

  def avgNumber(numbers: NumberAdapter*): NumberAdapter = /*
                                                            YOUR CODE HERE
                                                             */
    numbers.reduce((a, b) => a + b) / IntNumberAdapter(numbers.length)

  /**
    * Example d'utilisation de avgNumber pour une liste de Int
    */
  lazy val avgIntNumber = avgNumber(IntNumberAdapter(5), IntNumberAdapter(4), IntNumberAdapter(3), IntNumberAdapter(2), IntNumberAdapter(1))

  /**
    * Example d'utilisation de avgNumber pour une liste de Double
    */
  lazy val avgDoubleNumber = avgNumber(DoubleNumberAdapter(5.0), DoubleNumberAdapter(4.0), DoubleNumberAdapter(3.0), DoubleNumberAdapter(2.0), DoubleNumberAdapter(1.0))

  /*
  YOUR CODE HERE
   */
  case class IntNumberAdapter(i: Int) extends NumberAdapter {
    override def +(n: NumberAdapter): NumberAdapter = {
      n match {
        case IntNumberAdapter(anotherInt) =>
          IntNumberAdapter(i + anotherInt)
        case _ =>
          throw new scala.IllegalArgumentException(s"The parameter passed $n isn't an instance of IntNumberAdapter")
      }
    }

    override def /(n: NumberAdapter): NumberAdapter = {
      n match {
        case IntNumberAdapter(anotherInt) =>
          IntNumberAdapter(i / anotherInt)
        case _ =>
          throw new scala.IllegalArgumentException(s"The parameter passed $n isn't an instance of IntNumberAdapter")
      }
    }
  }

  case class DoubleNumberAdapter(d: Double) extends NumberAdapter {
    override def +(n: NumberAdapter): NumberAdapter = {
      n match {
        case DoubleNumberAdapter(anotherDouble) =>
          DoubleNumberAdapter(d + anotherDouble)
        case _ =>
          throw new scala.IllegalArgumentException(s"The parameter passed $n isn't an instance of DoubleNumberAdapter")
      }
    }

    override def /(n: NumberAdapter): NumberAdapter = {
      n match {
        case DoubleNumberAdapter(anotherDouble) =>
          DoubleNumberAdapter(d / anotherDouble)
        case _ =>
          throw new scala.IllegalArgumentException(s"The parameter passed $n isn't an instance of DoubleNumberAdapter")
      }
    }
  }

}

/**
  * Exemple de solution en Scala avec l'utilisation de la type class NumberLike
  */
object TypeClassAverage {

  /*
    Voici la type class NumberLike. Elle décrit les operations que sont applicables aux types qui font partie
    de cette type class. Elle ne décrit pas quels sont les types qui appartiennent à cette type class.
    */
  trait NumberLike[T] {
    def +(a: T, b: T): T

    def /(a: T, n: T): T
  }

  object NumberLike {

    /*
     Voici une déclaration d'instance d'une type class. C'est comme ça qu'on dit que le type Int appartient à la type class
     NumberLike.
     */
    implicit object IntNumberLike extends NumberLike[Int] {
      override def +(a: Int, b: Int): Int = a + b

      override def /(a: Int, n: Int): Int = a / n
    }

    /*
     Paréil pour le type Double
     */
    implicit object DoubleNumberLike extends NumberLike[Double] {
      override def +(a: Double, b: Double): Double = a + b

      override def /(a: Double, n: Double): Double = a / n
    }

  }

  /*
   avgNumber pour tous les T dont T est une instance de la type class NumberLike. Le paramètre converter ne fait pas partie
   du pattern, mais il est nécessaire pour pouvoir effectuer la division
   */
  def avgNumber[T](numbers: T*)(implicit numberLike: NumberLike[T], converter: Int => T) = {
    val sum = numbers.reduce((a, b) => numberLike.+(a, b))

    numberLike./(sum, converter(numbers.size))
  }

  /**
    * Example d'utilisation de avgNumber pour une liste de Int
    */
  lazy val avgIntNumber = avgNumber(5, 4, 3, 2, 1)

  /**
    * Example d'utilisation de avgNumber pour une liste de Double
    */
  lazy val avgDoubleNumber = avgNumber(5.0, 4.0, 3.0, 2.0, 1.0)

  /**
    * Example d'utilisation de avgNumber pour une liste de Int et Double mélangés
    */
  lazy val avgIntDoubleNumber = avgNumber(5.0, 4.0, 3.0, 2, 1)
}
