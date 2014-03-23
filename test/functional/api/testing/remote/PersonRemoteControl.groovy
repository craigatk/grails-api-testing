package api.testing.remote

import api.testing.Person
import grails.plugin.remotecontrol.RemoteControl

class PersonRemoteControl {
    RemoteControl remote = new RemoteControl()

    Person createPerson(Map params) {
        remote {
            new Person(params).save()
        }
    }

    Person findByFirstName(String firstName) {
        remote {
            Person.findByFirstName(firstName)
        }
    }
}
