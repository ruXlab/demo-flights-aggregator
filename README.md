
# Flights Aggregator API

Demo project featuring kotlin and spring boot

### How to run it?

Although it's _possible_ to run service it's pretty useless on it's own using `./gradlew bootRun` - there are no real
flights providers are available. So it's the best to run all tests (that includes e2e test for the API) using
`./gradlew test`, or use IntelliJ Idea's "All Test" launcher.

The original task was [posted here](https://github.com/barbradi/deblock-exercise)

## Assumptions

* It's still a toy exercise
* ToughAir response `tax` field is a percentage, same as `discount`. For instance, "49" (=0.49)
* There is **no validation** of the responses from the suppliers, in the toy project we assume they are ideal and ever fail.

## Design choices
     
### GeneticSupplier

The `GeneticSupplier` class helps to onboard suppliers that have a _simple_ REST interface. 
Any of its part can be overridden so it's pretty customizable. 

**"Why inheritance over composition?"** one could be curious. That was simple - the task
explicitly requested to favour OOP approach. It's fine as long as it's reasonable, consistent
accross the application and team is happy.

### Flights returned as a Stream

The `IFlightsSupplier` interface forces implementations to return java `Stream`. It's actually a good
idea since we don't know if some suppliers return or will dump gigabytes of data containing a billion flights. 
It could be `Iterable`, Kotlin's `flow` or RxJava's `Flowable`.

### Implementation of the `FlightsAggregator`

It's also implementing `IFlightsSupplier` interface but under the hood
it forwards and aggregates the request to other suppliers. It's nice to have
the same interface for aggregator and concrete implementations, isn't it? 
Liskov Substitution Principle works here well.

The _parallelism_ is archived via `parallelStream`. It's actually pretty good as a starting point as it's 
using the default thread pool(that's very big pool!). Obviously, in production we probably 
want to consolidate the parallel requests into the single pool.
     
### Flight Providers implementations: CrazyAir vs 

**CrazyAir** was initially codded during the pairing session so I'm not touching it,
although it could be good candidate for the generalisation using `GenericSupplier`.

### Testing

For the sake of demo there are number of unit, integration and e2e have been implemented.
Also, introduced some test helpers with kotlin DSL for the human-readable
dates formatting (like `fn(04 / MAY / 1984)`)
                                                     
The unit tests, integration and e2e tests are included.
                                                          
### Error handing

As it was discussed and implemented during the live pairing session
the `@ControllerAdvice` is a great way to handle errors globally on the application 
level, although it can't and shouldn't be a substitute for the all error handling.