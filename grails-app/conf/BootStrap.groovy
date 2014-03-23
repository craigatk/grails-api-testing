import api.testing.Person

class BootStrap {

    def init = { servletContext ->
        new Person(firstName: 'James', lastName: 'Bond').save()
    }
    def destroy = {
    }
}
