package api.testing
import api.testing.remote.PersonRemoteControl
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovyx.net.http.RESTClient
import org.apache.http.client.fluent.Request
import spock.lang.Specification

class DeletePersonApiFunctionalSpec extends Specification {
    PersonRemoteControl personRemoteControl

    def setup() {
        personRemoteControl = new PersonRemoteControl()
    }

    def 'should delete a person with Grails REST client builder'() {
        given:
        String firstNameToDelete = 'DeleteRest'
        Person person = personRemoteControl.createPerson([firstName: firstNameToDelete, lastName: 'Smith'])

        when:
        RestResponse response = new RestBuilder().delete("http://localhost:8080/grails-api-testing/person/${person.id}")

        then:
        assert response.status == 204
        assert !personRemoteControl.findByFirstName(firstNameToDelete)
    }

    def 'should delete a person with Groovy Http-Builder'() {
        given:
        String firstNameToDelete = 'DeleteHttp'
        Person person = personRemoteControl.createPerson([firstName: firstNameToDelete, lastName: 'Smith'])

        RESTClient restClient = new RESTClient("http://localhost:8080/grails-api-testing/")

        when:
        def response = restClient.delete(path: "person/${person.id}")

        then:
        assert response.status == 204
        assert !personRemoteControl.findByFirstName(firstNameToDelete)
    }

    def 'should delete a person with Apache Http Client Fluent'() {
        given:
        String firstNameToDelete = 'DeleteApache'
        Person person = personRemoteControl.createPerson([firstName: firstNameToDelete, lastName: 'Smith'])

        when:
        Request.Delete("http://localhost:8080/grails-api-testing/person/${person.id}").execute().returnContent()

        then:
        assert !personRemoteControl.findByFirstName(firstNameToDelete)
    }
}
