# test-support-lib
A lightweight library that provides a modular testing infrastructure framework that unifies Testcontainers, WireMock, JMS test utilities, and other test-time integrations under a single, extensible annotation model.

The library automatically adapts to both Spring TestContext and JUnit Jupiter lifecycles, enabling seamless initialization, startup, and cleanup of test resources without any framework-specific boilerplate.


## How to use it
* Clone the project to your local machine.

```bash
  git clone https://github.com/mibrahim-iti/test-support-lib.git
```

* build a jar dependency to use into your project
* 
 ```bash
  mvn clean install
```
* Add the next dependency to your project
```bash
<dependency>
  <groupId>com.test.support.lib</groupId>
  <artifactId>test-support-lib-containers</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```

* Use it in top of your @SpringBootTest for example
```bash
@SpringBootTest
@EnableTestContainers({ContainerType.ARTEMIS, ContainerType.DYNAMODB})
class DemoApplicationTests {
	@Test
	void testContainersTest() {
		Assertions.assertNotNull(ContainerRegistry.getArtemis());
		Assertions.assertNotNull(ContainerRegistry.getDynamoDb());
		Assertions.assertThrows(ContainerNotEnabledException.class, ContainerRegistry::getSqs);
	}
}
```
