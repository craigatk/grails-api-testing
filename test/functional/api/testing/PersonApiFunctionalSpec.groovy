package api.testing
import api.testing.remote.PersonRemoteControl
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import org.apache.http.client.fluent.Content
import org.apache.http.client.fluent.Request
import spock.lang.IgnoreRest
import spock.lang.Specification

class PersonApiFunctionalSpec extends Specification {
    PersonRemoteControl personRemoteControl

    def setup() {
        personRemoteControl = new PersonRemoteControl()
    }

    @IgnoreRest
    def 'should fetch a single person'() {
        given:
        Person person = personRemoteControl.createPerson([firstName: 'Jim', lastName: 'Smith'])

        RestBuilder rest = new RestBuilder()

        when:
        def resp = rest.get("http://localhost:8080/grails-api-testing/person/${person.id}") {
            accept JSON
        }

        then:
        assert resp.status == 200
        assert resp.json.firstName == 'Jim'
        assert resp.json.lastName == 'Smith'
    }


    def 'fetch single person with apache http client'() {
        given:
        Person person = personRemoteControl.createPerson([firstName: 'Apache', lastName: 'Httpclient'])

        when:
        Content response = Request.Get("http://localhost:8080/grails-api-testing/person/${person.id}").execute().returnContent()

        def json = JSON.parse(response.toString())

        then:
        assert json.firstName == person.firstName
        assert json.lastName == person.lastName
    }

    /*def 'fetch single person with groovy http builder'() {
        given:
        Person person = personRemoteControl.createPerson([firstName: 'Groovy', lastName: 'Httpbuilder'])

        new RESTClient("http://localhost:8080/grails-api-testing/person/${person.id}")

        when:

        then:
    }*/
}
