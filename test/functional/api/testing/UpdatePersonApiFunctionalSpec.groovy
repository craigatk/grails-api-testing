package api.testing
import api.testing.remote.PersonRemoteControl
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import groovyx.net.http.RESTClient
import org.apache.http.client.fluent.Request
import spock.lang.Specification

class UpdatePersonApiFunctionalSpec extends Specification {
    PersonRemoteControl personRemoteControl

    def setup() {
        personRemoteControl = new PersonRemoteControl()
    }

    def 'should update a person with Grails REST client builder'() {
        given:
        String startingFirstName = "StartingRest"
        String startingLastName = "Smith"
        String updatedFirstName = "UpdatedRest"
        String updatedLastName = "Jones"

        Person person = personRemoteControl.createPerson([firstName: startingFirstName, lastName: startingLastName])

        when:
        def response =  new RestBuilder().put("http://localhost:8080/grails-api-testing/person/${person.id}") {
            json {
                firstName = updatedFirstName
                lastName = updatedLastName
            }
        }

        then:
        assert response.status == 200
        assert personRemoteControl.findByFirstName(updatedFirstName)?.lastName == updatedLastName
    }

    def 'should update a person with Groovy Http-Builder'() {
        given:
        String startingFirstName = "StartingHttp"
        String startingLastName = "Smith"
        String updatedFirstName = "UpdatedHttp"
        String updatedLastName = "Jones"

        Person person = personRemoteControl.createPerson([firstName: startingFirstName, lastName: startingLastName])

        RESTClient restClient = new RESTClient("http://localhost:8080/grails-api-testing/")

        when:
        def response = restClient.put(
                path: "person/${person.id}",
                body: [firstName: updatedFirstName, lastName: updatedLastName],
                contentType: groovyx.net.http.ContentType.JSON
        )

        then:
        assert response.status == 200
        assert personRemoteControl.findByFirstName(updatedFirstName)?.lastName == updatedLastName
    }

    def 'should update a person with Apache Http Client Fluent'() {
        given:
        String startingFirstName = "StartingApache"
        String startingLastName = "Smith"
        String updatedFirstName = "UpdatedApache"
        String updatedLastName = "Jones"

        Person person = personRemoteControl.createPerson([firstName: startingFirstName, lastName: startingLastName])

        String jsonString = new JSON(firstName: updatedFirstName, lastName: updatedLastName).toString()

        when:
        Request.Put("http://localhost:8080/grails-api-testing/person/${person.id}")
                .bodyString(jsonString, org.apache.http.entity.ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
        then:
        assert personRemoteControl.findByFirstName(updatedFirstName)?.lastName == updatedLastName
    }
}
