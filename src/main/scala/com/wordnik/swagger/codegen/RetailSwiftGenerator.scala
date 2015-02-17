package com.wordnik.swagger.codegen

import com.wordnik.swagger.codegen.model._

object RetailSwiftGenerator extends RetailSwiftGenerator {
  def main(args: Array[String]) = generateClient(args)
}

class RetailSwiftGenerator extends SwiftGenerator {
  // naming for the models
  override def toModelName(name: String) = {
    val modelName = super.toModelName(name)
    
    modelName.startsWith("RIPage") match {
      case true => {
        "RI" + modelName.substring(6) + "Page"
      }
      case _ => modelName
    }
  }
  
  // model classes
  // override def processModelMap(m: Map[String, AnyRef]): Map[String, AnyRef] = {
  //   val map = scala.collection.mutable.HashMap() ++ super.processModelMap(m)
  //
  //   map.get("classname") match {
  //     case Some(classname: String) => {
  //       classname.endsWith("Page") match {
  //         case true => {
  //           val hashType = scala.collection.mutable.HashMap[String,AnyRef]
  //           map.get("vars") match {
  //             case Some(vars: scala.collection.mutable.ListBuffer[hashType]) => {
  //               vars.map(v => {
  //                 v.get("name") match {
  //                   case Some(name: String) => {
  //                     v.get("baseDataType") match {
  //                       case Some(baseDataType: String) => {
  //                         map += "isPage" -> "true"
  //                         map += "pageModelType" -> baseDataType
  //                       }
  //                       case _ =>
  //                     }
  //                   }
  //                   case _ =>
  //                 }
  //               })
  //             }
  //             case _ =>
  //           }
  //         }
  //         case _ =>
  //       }
  //     }
  //     case _ =>
  //   }
  //
  //   return map.toMap
  // }
}
