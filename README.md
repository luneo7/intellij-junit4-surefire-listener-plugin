# IntelliJ Junit4 Surefire Listener

<!-- Plugin description -->
**Junit4 Surefire Listener** is a plugin that will read your Maven POM file (when imported/refreshed in IntelliJ) 
[Maven Surefire Listener config](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html#Using_Custom_Listeners_and_Reporters),
 and when the test is run it will inject the configured listeners, we just have some limitations that IntelliJ uses some wrappers 
([IDEAJUnitListenerEx](https://github.com/JetBrains/intellij-community/blob/master/java/java-runtime/src/com/intellij/rt/execution/junit/IDEAJUnitListenerEx.java) 
and [IDEAJUnitListener](https://github.com/JetBrains/intellij-community/blob/master/java/java-runtime/src/com/intellij/rt/execution/junit/IDEAJUnitListener.java))
that removes the default parameters of the [JUnit 4 RunListener](https://github.com/junit-team/junit4/blob/main/src/main/java/org/junit/runner/notification/RunListener.java)
because of that we don't have all the original parameters, but we can use the events to trigger some instantiation in the `testRunStarted` for instance.
<!-- Plugin description end -->

