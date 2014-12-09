package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.model._

object SwiftGenerator extends SwiftGenerator {
  def main(args: Array[String]) = generateClient(args)
}

class SwiftGenerator extends BasicGenerator {
  override def defaultIncludes = Set(
    "time_t",
    "String",
    "Int",
    "Double", 
    "Float",
    "Bool")

  override def languageSpecificPrimitives = Set(
      "time_t",
      "String",
      "Int",
      "Double",
      "Float",
      "Bool",
      "Array")

  override def reservedWords = Set("void", "char", "short", "int", "void", "char", "short", "int", "long", "float", "double", "signed", "unsigned", "id", "const", "volatile", "in", "out", "inout", "bycopy", "byref", "oneway", "self", "super")
  
  override def typeMapping = Map(
    "enum" -> "NSString",
    "date" -> "time_t",
    "Date" -> "time_t",
    "boolean" -> "Bool",
    "string" -> "String",
    "integer" -> "Int",
    "int" -> "Int",
    "float" -> "Float",
    "long" -> "Int",
    "double" -> "Double",
    "object" -> "NSObject")

  override def toModelFilename(name: String) = "RI" + name

  // naming for the models
  override def toModelName(name: String) = {
    (typeMapping.keys ++ 
      importMapping.values ++ 
      defaultIncludes ++ 
      languageSpecificPrimitives
    ).toSet.contains(name) match {
      case true => name(0).toUpper + name.substring(1)
      case _ => {
        "RI" + name(0).toUpper + name.substring(1)
      }
    }
  }

  // objective c doesn't like variables starting with "new"
  override def toVarName(name: String): String = {
    val paramName = name.replaceAll("[^a-zA-Z0-9_]","")
    
    val identifierFiltered = paramName match {
        case "id" => "identifier"
        case other => other
    }
    
    val identifierExpanded = identifierFiltered.replaceAll("Id", "Identifier")
    val acronymsCapitalized = identifierExpanded.replaceAll("Url", "URL")
    
    if(reservedWords.contains(acronymsCapitalized)) {
      escapeReservedWord(acronymsCapitalized)
    }
    else acronymsCapitalized
  }

  // naming for the apis
  override def toApiName(name: String) = "RI" + name(0).toUpper + name.substring(1) + "API"

  // location of templates
  override def templateDir = "swift"

  // template used for models
  modelTemplateFiles += "model.mustache" -> ".swift"

  // template used for apis
  apiTemplateFiles += "api.mustache" -> ".swift"

  // package for models
  override def invokerPackage: Option[String] = None

  // package for models
  override def modelPackage: Option[String] = None

  // package for api classes
  override def apiPackage: Option[String] = None

  // response classes
  override def processResponseClass(responseClass: String): Option[String] = {
    typeMapping.contains(responseClass) match {
      case true => Some(typeMapping(responseClass))
      case false => {
        responseClass match {
          case "void" => None
          case e: String => {
            if(responseClass.toLowerCase.startsWith("array") || responseClass.toLowerCase.startsWith("list"))
              Some("NSArray")
            else
              Some(toModelName(responseClass))
          }
        }
      }
    }
  }

  override def processApiMap(m: Map[String, AnyRef]): Map[String, AnyRef] = {
    val mutable = scala.collection.mutable.Map() ++ m
    mutable += "newline" -> "\n"

    mutable.map(k => {
      k._1 match {
        case e: String if (e == "allParams") => {
          val sp = (mutable(e)).asInstanceOf[List[_]]
          sp.size match {
            case i: Int if(i > 0) => mutable += "hasParams" -> "true"
            case _ =>
          }
        }
        case _ =>
      }
    })
    mutable.toMap
  }

  override def processResponseDeclaration(responseClass: String): Option[String] = {
    processResponseClass(responseClass) match {
      case Some("void") => Some("void")
      case Some(e) => Some(e + "*")
      case _ => Some(responseClass)
    }
  }

  override def toDeclaredType(dt: String): String = {
    val t = typeMapping.getOrElse(dt, dt)
    toModelName(t)
  }

  override def toDeclaration(obj: ModelProperty) = {
    val declaredType = toDeclaredType(obj.`type`)
    val defaultValue = toDefaultValue(declaredType, obj)
    
    val expandedType = declaredType match {
      case "Array" => {
        val innerType = obj.items match {
          case Some(items) => {
            items.ref match {
              case Some(ref) => ref
              case _ => items.`type`
            }
          }
          case _ => {
            println("failed on " + obj)
            throw new Exception("no inner type defined")
          }
        }
        
        "[" + toDeclaredType(innerType) + "]"
      }
      case _ => declaredType
    }
    
    (expandedType, defaultValue)
  }

  override def escapeReservedWord(word: String) = "_" + word

  override def toDefaultValue(properCase: String, obj: ModelProperty) = {
    properCase match {
      case "boolean" => "false"
      case "int" => "0"
      case "long" => "0"
      case "float" => "0"
      case "double" => "0"
      case "Array" => "[]"
      case _ => "nil"
    }
  }
}
