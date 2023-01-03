#!/bin/groovy
print "> "
System.in.eachLine() { line ->
   if(line.equals("")){
      System.exit(0)
   }else{
      nvps = line.split("&")
      nvps.sort()
      nvps.each {
         nvp = it.split("=")
         name = nvp[0]
         if(nvp.length > 1){
            value = java.net.URLDecoder.decode(nvp[1], "UTF-8")
            println "$name=$value"
         }else{
            println "$name"
         }
      }
      print "> "
   }
}

