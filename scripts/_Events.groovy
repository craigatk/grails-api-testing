// If you have another plugin that enables functional tests, such as Geb, you won't need to do this
eventAllTestsStart = {
    if (getBinding().variables.containsKey("functionalTests")) {
        functionalTests << "functional"
    }
}