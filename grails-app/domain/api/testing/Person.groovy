package api.testing

// <gist id="9875403">
import grails.rest.Resource

@Resource(uri = '/person', formats = ['json'])
class Person implements Serializable {
  String firstName
  String lastName

  static constraints = {
    firstName(nullable: false)
    lastName(nullable: false)
  }
}
// </gist>
