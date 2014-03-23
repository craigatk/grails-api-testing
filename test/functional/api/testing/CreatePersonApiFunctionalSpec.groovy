package api.testing
import api.testing.remote.PersonRemoteControl
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import groovyx.net.http.RESTClient
import org.apache.http.client.fluent.Request
import spock.lang.Specification

class CreatePersonApiFunctionalSpec extends Specification {
    PersonRemoteControl personRemoteControl

    def setup() {
        personRemoteControl = new PersonRemoteControl()
    }

    def 'should create a person with Grails REST client builder'() {
        given:
        String newFirstName = "RestNew"
        String newLastName = "Smith"

        when:
        def response =  new RestBuilder().post("http://localhost:8080/grails-api-testing/person") {
            json {
                firstName = newFirstName
                lastName = newLastName
            }
        }

        then:
        assert response.status == 201
        assert personRemoteControl.findByFirstName(newFirstName)?.lastName == newLastName
    }

    def 'should create a person with Groovy Http-Builder'() {
        given:
        String newFirstName = "HttpNew"
        String newLastName = "Smith"

        RESTClient restClient = new RESTClient("http://localhost:8080/grails-api-testing/")

        when:
        def response = restClient.post(
                path: "person",
                body: [firstName: newFirstName, lastName: newLastName],
                contentType: groovyx.net.http.ContentType.JSON
        )

        then:
        assert response.status == 201
        assert personRemoteControl.findByFirstName(newFirstName)?.lastName == newLastName
    }

    def 'should create a new person with Apache Http Client Fluent'() {
        given:
        String newFirstName = "ApacheNew"
        String newLastName = "Smith"

        String jsonString = new JSON(firstName: newFirstName, lastName: newLastName).toString()

        when:
        Request.Post("http://localhost:8080/grails-api-testing/person")
                .bodyString(jsonString, org.apache.http.entity.ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
        then:
        assert personRemoteControl.findByFirstName(newFirstName)?.lastName == newLastName
    }
}
