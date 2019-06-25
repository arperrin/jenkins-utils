// #!/usr/bin/groovy
package com.perrin.jenkinsUtils;

@Grab(group='org.apache.httpcomponents', module='httpclient', version='4.5.9')

import groovy.json.*

import org.apache.http.HttpHeaders
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.apache.http.entity.StringEntity

def broken(keyword = '*', repository = '*'){
  def url = "http://192.168.33.10:8081/service/rest/v1/search?sort=version&direction=desc&q=${keyword}&repository=${repository}"
  println "url: ${url}"

  HttpClientBuilder.create().build().withCloseable { client -> 
    final request = new HttpGet(url)

    client.execute(request).withCloseable { response ->
      assert response.statusLine.statusCode == 200
    }
  }
}

def search(keyword = '*', repository = '*'){
  def url = "http://192.168.33.10:8081/service/rest/v1/search?sort=version&direction=desc&q=${keyword}&repository=${repository}"
  def request = RequestBuilder
    .create("GET")
    .setUri(url)
    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    .build()

  def client = HttpClientBuilder.create().build()
  def response = client.execute(request)
  if (response.statusLine.statusCode != 200) {
    println "Error from server: ${response.statusLine.statusCode}"
  }

  def bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
  def jsonResponse = bufferedReader.getText()
  println "response: \n" + jsonResponse
}

return this;