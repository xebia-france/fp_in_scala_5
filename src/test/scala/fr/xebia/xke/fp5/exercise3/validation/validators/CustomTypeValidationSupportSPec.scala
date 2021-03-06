package fr.xebia.xke.fp5.exercise3.validation.validators

import fr.xebia.xke.fp5.exercise3.model.Person
import fr.xebia.xke.fp5.exercise3.validation.Validator.validate
import fr.xebia.xke.fp5.exercise3.validation.validators.CustomTypeValidationSupport._
import org.scalatest.{FlatSpec, Matchers}

class CustomTypeValidationSupportSpec extends FlatSpec with Matchers {

  "PersonValidationSupport" should "accept valid person" in {
    validate(Person("bob", "marley", None)).isSuccess should be(true)
  }

  it should "accept valid person with mail" in {
    validate(Person("bob", "marley", Option("bob.marley@gmail.com"))).isSuccess should be(true)
  }

  it should "fail for invalid name" in {
    validate(Person("bob42", "marley", None)).isFailure should be(true)
  }

  it should "fail for invalid mail" in {
    validate(Person("bob42", "marley", Option("bob@smoke.weed"))).isFailure should be(true)
  }

}
