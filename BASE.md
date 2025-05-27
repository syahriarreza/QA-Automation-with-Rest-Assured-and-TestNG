```
src/
├── test/
│   ├── java/
│   │   └── cucumber/
│   │       ├── base/
│   │       │   └── BaseTest.java
│   │       ├── dto/
│   │       │   ├── UserRequest.java          <-- pakai Lombok
│   │       │   └── LoginResponse.java        <-- pakai Lombok
│   │       ├── hooks/
│   │       │   └── Hooks.java
│   │       ├── runner/
│   │       │   └── TestRunner.java           <-- @CucumberOptions
│   │       └── steps/
│   │           ├── UserSteps.java
│   │           └── ObjectSteps.java
│   └── resources/
│       ├── features/
│       │   ├── user.feature
│       │   └── object.feature
│       ├── schemas/
│       │   └── login-schema.json             <-- JSON Schema validator
│       └── cucumber_suite.xml                <-- TestNG suite runner
└── pom.xml

```