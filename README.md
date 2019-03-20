# Sample app for coding dojo on reactive Java

Launch server with
```
./mvnw spring-boot:run
```

Make a non-streaming request of users
```sh
curl "http://localhost:8080/users?limit=2"
[
    {
        "uuid": "d000c3db-68a1-412d-b83e-6a7f3f3aa223"
    },
    {
        "uuid": "48d1ae5b-10f1-4284-9bef-7491b51e2488"
    }
]
```

Make a streaming request of users
```sh
curl http://localhost:8080/users -H "Accept: application/stream+json"
{
  "uuid" : "9fca1b03-1a2e-4ace-b03b-17e2db03b44d"
}
{
  "uuid" : "55d5e639-a6be-4015-b1e8-a7c1e9654e8c"
}
{
  "uuid" : "8858180a-089d-42f7-a764-4b89dbfd60c3"
}
```

Make a streaming request of userProducts
```sh
curl http://localhost:8080/userProducts
{
    "product": {
        "uuid": "af120ae6-ec01-47d7-ac4a-c8b996ad92a3"
    },
    "user": {
        "uuid": "0ba52e63-c617-4023-adfe-e75f1ade071d"
    }
}
{
    "product": {
        "uuid": "563c34fd-1092-4e23-ac86-0285a0bbcc77"
    },
    "user": {
        "uuid": "2622ab5b-50a2-4023-b4b3-7c384eb08f65"
    }
}
...
```
