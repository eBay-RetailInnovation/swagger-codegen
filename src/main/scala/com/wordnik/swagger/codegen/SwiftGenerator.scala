package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.model._

object SwiftGenerator extends SwiftGenerator {
  def main(args: Array[String]) = generateClient(args)
}

class SwiftGenerator extends BasicGenerator {
    // location of templates
    override def templateDir = "swift"
    
    // template used for models
    modelTemplateFiles += "model.mustache" -> ".swift"

    // template used for apis
    apiTemplateFiles += "api.mustache" -> ".swift"
}