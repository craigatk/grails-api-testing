package api.testing

import grails.rest.Resource

@Resource(uri='/person', formats=['json'])
class Person implements Serializable {
    String firstName
    String middleName
    String lastName

    static constraints = {
        firstName(nullable: false)
        middleName(nullable: true)
        lastName(nullable: false)
    }
}
