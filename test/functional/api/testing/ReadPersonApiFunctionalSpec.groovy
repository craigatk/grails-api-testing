package api.testing
import api.testing.remote.PersonRemoteControl
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovyx.net.http.RESTClient
import org.apache.http.client.fluent.Content
import org.apache.http.client.fluent.Request
import spock.lang.Specification

class ReadPersonApiFunctionalSpec extends Specification {
    PersonRemoteControl personRemoteControl

    def setup() {
        personRemoteControl = new PersonRemoteControl()
    }

    def 'should fetch person with Grails REST client builder'() {
        given:
        Person person = personRemoteControl.createPerson([firstName: 'Rest', lastName: 'Smith'])

        when:
        RestResponse response = new RestBuilder().get("http://localhost:8080/grails-api-testing/person/${person.id}") {
            // Need to set the accept content-type to JSON, otherwise it defaults to String and
            // the API will throw a 415 'unsupported media type' error
            accept JSON
        }

        then:
        assert response.status == 200
        assert response.json.firstName == person.firstName
        assert response.json.lastName == person.lastName
    }

    def 'should fetch a person with Groovy Http-Builder'() {
        given:
        Person person = personRemoteControl.createPerson([firstName: 'Httpbuilder', lastName: 'Smith'])

        RESTClient restClient = new RESTClient("http://localhost:8080/grails-api-testing/")

        when:
        def response = restClient.get(path: "person/${person.id}")

        then:
        assert response.status == 200
        assert response.data.firstName == person.firstName
        assert response.data.lastName == person.lastName
    }

    def 'should fetch a person with Apache Http Client Fluent'() {
        given:
        Person person = personRemoteControl.createPerson([firstName: 'Httpclient', lastName: 'Smith'])

        when:
        Content responseContent = Request.Get("http://localhost:8080/grails-api-testing/person/${person.id}").execute().returnContent()

        def json = JSON.parse(responseContent.toString())

        then:
        assert json.firstName == person.firstName
        assert json.lastName == person.lastName
    }
}
