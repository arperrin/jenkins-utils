// #!/usr/bin/groovy
package com.perrin.jenkinsUtils;

@Grab(group='org.apache.httpcomponents', module='httpclient', version='4.5.2')

import groovy.json.*

import org.apache.http.client.methods.*
import org.apache.http.entity.*
import org.apache.http.impl.client.*

def search(keyword,repository){
  def url = "http://192.168.33.10:8081/service/rest/v1/searc?sort=version&direction=desc&q=${keyword}&repository=${repository}"

  HttpClientBuilder.create().build().withCloseable { client -> 
    final request = new HttpGet(url)
    request.addHeader("content-type", "application/json")

    client.execute(request).withCloseable{ response ->
      if (response.statusLine.statusCode != 200){
        println "Error from server: ${response.statusLine.statusCode}"
      }

      def bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
      def jsonResponse = bufferedReader.getText()
      println "response: \n" + jsonResponse
    }
  }
}

return this;