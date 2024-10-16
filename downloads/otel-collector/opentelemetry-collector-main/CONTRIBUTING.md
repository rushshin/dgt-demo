# Contributing Guide

We'd love your help! Please join our weekly [SIG
meeting](https://github.com/open-telemetry/community#special-interest-groups).

## Target audiences

The OpenTelemetry Collector has three main target audiences:

1. *End-users*, aiming to use an OpenTelemetry Collector binary.
1. *Component developers*, consuming the Go APIs to create components compatible with the OpenTelemetry Collector Builder.
1. *Collector library users*, consuming other Go APIs exposed by the opentelemetry-collector repository, for example to
   build custom distributions or other projects building on top of the Collector Go APIs.

When the needs of these audiences conflict, end-users should be prioritized, followed by component developers, and
finally Collector library users.

### End-users

End-users are the target audience for our binary distributions, as made available via the
[opentelemetry-collector-releases](https://github.com/open-telemetry/opentelemetry-collector-releases) repository, as
well as distributions created using the [OpenTelemetry Collector
Builder](https://github.com/open-telemetry/opentelemetry-collector/tree/main/cmd/builder). To them, stability in the
behavior is important, be it runtime, configuration, or [internal
telemetry](https://opentelemetry.io/docs/collector/internal-telemetry/). They are more numerous and harder to get in
touch with, making our changes to the Collector more disruptive to them than to other audiences. As a general rule,
whenever you are developing OpenTelemetry Collector components (extensions, receivers, processors, exporters,
connectors), you should have end-users' interests in mind. Similarly, changes to code within packages like `config` will
have an impact on this audience. Make sure to cause minimal disruption when doing changes here.

### Component developers

Component developers create new extensions, receivers, processors, exporters, and connectors to be used with the
OpenTelemetry Collector. They are the primary audience for the opentelemetry-collector repository's public Go API. A
significant part of them will contribute to opentelemetry-collector-contrib. In addition to the end-user aspect
mentioned above, this audience also cares about Go API compatibility of Go modules such as the ones in the `pdata`,
`component`, `consumer`, `confmap`, `exporterhelper`, `config*` modules and others, even though such changes wouldn't cause any
impact to end-users. See the [Breaking changes](#breaking-changes) in this document for more information on how to perform changes
affecting this audience.

### Collector library users

A third audience uses the OpenTelemetry Collector as a library to build their own distributions or other projects based
on the Collector. This audience is the main consumer of modules such as `service` or `otelcol`. They also share the same
concerns as component developers regarding Go API compatibility and are likewise interested in behavior stability. These
are our most advanced users and are the most equipped to deal with disruptive changes.

## How to structure PRs to get expedient reviews?

We recommend that any PR (unless it is trivial) to be smaller than 500 lines
(excluding go mod/sum changes) in order to help reviewers to do a thorough and
reasonably fast reviews.

### When adding a new component

Components refer to connectors, exporters, extensions, processors, and receivers. The key criteria for implementing a component is to:

* Implement the `component.Component` interface
* Provide a configuration structure which defines the configuration of the component
* Provide the implementation that performs the component operation

For more details on components, see the [Adding New Components](https://github.com/open-telemetry/opentelemetry-collector-contrib/blob/main/CONTRIBUTING.md#adding-new-components) document and the tutorial [Building a Trace Receiver](https://opentelemetry.io/docs/collector/trace-receiver/) which provides a detailed example of building a component.

When adding a new component to the OpenTelemetry Collector, ensure that any configuration structs used by the component include fields with the `configopaque.String` type for sensitive data. This ensures that the data is masked when serialized to prevent accidental exposure.

When submitting a component to the community, consider breaking it down into separate PRs as follows:

* **First PR** should include the overall structure of the new component:
  * Readme, configuration, and factory implementation should usually use the helper
    factory structs.
  * This PR is usually trivial to review, so the size limit does not apply to
    it.
  * The component should use [`In Development` Stability](https://github.com/open-telemetry/opentelemetry-collector#development) in its README.
* **Second PR** should include the concrete implementation of the component. If the
  size of this PR is larger than the recommended size consider splitting it into
  multiple PRs.
* **Last PR** should mark the new component as `Alpha` stability and add it to the `otelcorecol`
  binary by updating the `otelcorecol/components.go` file. The component must be enabled
  only after sufficient testing and only when it meets [`Alpha` stability requirements.](https://github.com/open-telemetry/opentelemetry-collector#alpha)
* Once a new component has been added to the executable, please add the component
  to the [OpenTelemetry.io registry](https://github.com/open-telemetry/opentelemetry.io#adding-a-project-to-the-opentelemetry-registry).
* intra-repository `replace` statements in `go.mod` files can be automatically inserted by running `make crosslink`. For more information
  on the `crosslink` tool see the README [here](https://github.com/open-telemetry/opentelemetry-go-build-tools/tree/main/crosslink).

### Refactoring Work

Any refactoring work must be split in its own PR that does not include any
behavior changes. It is important to do this to avoid hidden changes in large
and trivial refactoring PRs.

## Report a bug or request a feature

Reporting bugs is an important contribution. Please make sure to include:

* Expected and actual behavior
* The OpenTelemetry version you are running
* If possible, steps to reproduce

## How to contribute

### Before you start

Please read the project contribution
[guide](https://github.com/open-telemetry/community/tree/main/guides/contributor)
for general practices for the OpenTelemetry project.

Select a good issue from the links below (ordered by difficulty/complexity):

* [Good First Issue](https://github.com/open-telemetry/opentelemetry-collector/issues?utf8=%E2%9C%93&q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22)
* [Help Wanted](https://github.com/open-telemetry/opentelemetry-collector/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)

Comment on the issue that you want to work on so we can assign it to you and
clarify anything related to it.

If you would like to work on something that is not listed as an issue
(e.g. a new feature or enhancement) please first read our [vision](docs/vision.md) 
to make sure your proposal aligns with the goals of the
Collector, then create an issue and describe your proposal. It is best to do this
in advance so that maintainers can decide if the proposal is a good fit for
this repository. This will help avoid situations when you spend significant time
on something that maintainers may decide this repo is not the right place for.

If you're new to the Collector, the [internal architecture](docs/internal-architecture.md) documentation may be helpful.

Follow the instructions below to create your PR.

### Fork

In the interest of keeping this repository clean and manageable, you should
work from a fork. To create a fork, click the 'Fork' button at the top of the
repository, then clone the fork locally using `git clone
git@github.com:USERNAME/opentelemetry-collector.git`.

You should also add this repository as an "upstream" repo to your local copy,
in order to keep it up to date. You can add this as a remote like so:

`git remote add upstream https://github.com/open-telemetry/opentelemetry-collector.git`

Verify that the upstream exists:

`git remote -v`

To update your fork, fetch the upstream repo's branches and commits, then merge
your `main` with upstream's `main`:

```
git fetch upstream
git checkout main
git merge upstream/main
```

Remember to always work in a branch of your local copy, as you might otherwise
have to contend with conflicts in `main`.

Please also see [GitHub
workflow](https://github.com/open-telemetry/community/blob/main/guides/contributor/processes.md#github-workflow)
section of the general project contributing guide.

## Required Tools

Working with the project sources requires the following tools:

1. [git](https://git-scm.com/)
2. [go](https://golang.org/) (version 1.22 and up)
3. [make](https://www.gnu.org/software/make/)
4. [docker](https://www.docker.com/)

## Repository Setup

Fork the repo and checkout the upstream repo to your GOPATH by:

```
$ git clone git@github.com:open-telemetry/opentelemetry-collector.git
```

Add your fork as an origin:

```shell
$ cd opentelemetry-collector
$ git remote add fork git@github.com:YOUR_GITHUB_USERNAME/opentelemetry-collector.git
```

Run tests, fmt, and lint:

```shell
$ make
```

## Creating a PR

Checkout a new branch, make modifications, build locally, and push the branch to your fork
to open a new PR:

```shell
$ git checkout -b feature
# edit
$ make
$ make fmt
$ git commit
$ git push fork feature
```

### Commit Messages

Use descriptive commit messages. Here are [some recommendations](https://cbea.ms/git-commit/)
on how to write good commit messages.
When creating PRs GitHub will automatically copy commit messages into the PR description,
so it is a useful habit to write good commit messages before the PR is created.
Also, unless you actually want to tell a story with multiple commits make sure to squash
into a single commit before creating the PR.

When maintainers merge PRs with multiple commits, they will be squashed and GitHub will
concatenate all commit messages right before you hit the "Confirm squash and merge"
button. Maintainers must make sure to edit this concatenated message to make it right before merging.
In some cases, if the commit messages are lacking the easiest approach to have at
least something useful is copy/pasting the PR description into the commit message box
before merging (but see the above paragraph about writing good commit messages in the first place).

## General Notes

This project uses Go 1.22.* and [Github Actions.](https://github.com/features/actions)

It is recommended to run `make gofmt all` before submitting your PR.

## Coding Guidelines

We consider the OpenTelemetry Collector to be close to production quality and the quality bar
for contributions is set accordingly. Contributions must have readable code written
with maintainability in mind (if in doubt check [Effective Go](https://golang.org/doc/effective_go.html)
for coding advice). The code must adhere to the following robustness principles that
are important for software that runs autonomously and continuously without direct
interaction with a human (such as this Collector).

### Naming convention

To keep naming patterns consistent across the project, naming patterns are enforced to make intent clear by:

- Methods that return a variable that uses the zero value or values provided via the method MUST have the prefix `New`. For example:
  - `func NewKinesisExporter(kpl aws.KinesisProducerLibrary)` allocates a variable that uses
    the variables passed on creation.
  - `func NewKeyValueBuilder()` SHOULD allocate internal variables to a safe zero value.
- Methods that return a variable that uses non-zero value(s) that impacts business logic MUST use the prefix `NewDefault`. For example:
  - `func NewDefaultKinesisConfig()` would return a configuration that is the suggested default
    and can be updated without concern of causing a race condition.
- Methods that act upon an input variable MUST have a signature that reflects concisely the logic being done. For example:
  - `func FilterAttributes(attrs []Attribute, match func(attr Attribute) bool) []Attribute` MUST only filter attributes out of the passed input
    slice and return a new slice with values that `match` returns true. It may not do more work than what the method name implies, ie, it
    must not key a global history of all the slices that have been filtered.
- Methods that get the value of a field i.e. a getterMethod MUST use an uppercase first letter and NOT a `get` prefix. For example:
  - `func (p *Person) Name() string {return p.name} ` Name (with an uppercase N, exported) method is used here to get the value of the name field and not `getName`.The use of upper-case names for export provides the hook to discriminate the field from the method.
- Methods that set the value of a field i.e. a setterMethod MUST use a `set` prefix. For example:
  - `func (p *Person) SetName(newName string) {p.name = newName}` SetName method here sets the value of the name field.
- Variable assigned in a package's global scope that is preconfigured with a default set of values MUST use `Default` as the prefix. For example:
  - `var DefaultMarshallers = map[string]pdata.Marshallers{...}` is defined with an exporter package that allows for converting an encoding name,
    `zipkin`, and return the preconfigured marshaller to be used in the export process.
- Types that are specific to a signal MUST be worded with the signal used as an adjective, i.e. `SignalType`. For example:
  - `type TracesSink interface {...}`
- Types that deal with multiple signal types should use the relationship between the signals to describe the type, e.g. `SignalToSignalType` or `SignalAndSignalType`. For example:
  - `type TracesToTracesFunc func(...) ...`
- Functions dealing with specific signals or signal-specific types MUST be worded with the signal or type as a direct object, i.e. `VerbSignal`, or `VerbType` where `Type` is the full name of the type including the signal name. For example:
  - `func ConsumeTraces(...) {...}`
  - `func CreateTracesExport(...) {...}`
  - `func CreateTracesToTracesFunc(...) {...}`

### Enumerations

To keep naming patterns consistent across the project, enumeration patterns are enforced to make intent clear:

- Enumerations should be defined using a type definition, such as `type Level int32`.
- Enumerations should use either `int` or `string` as the underlying type
- The enumeration name should succinctly describe its purpose
  - If the package name represents the entity described by the enumeration then the package name should be factored into the name of the enumeration.  For example, `component.Type` instead of `component.ComponentType`.
  - The name should convey a sense of limited categorization. For example, `pcommon.ValueType` is better than `pcommon.Value` and `component.Kind` is better than `component.KindType`, since `Kind` already conveys categorization.
- Constant values of an enumeration should be prefixed with the enumeration type name in the name:
  - `pcommon.ValueTypeStr` for `pcommon.ValueType`
  - `pmetric.MetricTypeGauge` for `pmetric.MetricType`


### Recommended Libraries / Defaults

In order to simplify development within the project, we have made certain library recommendations that should be followed.

| Scenario 	 | Recommended                   	                | Rationale                                                                                                                  |
|------------|------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| Hashing  	 | ["hashing/fnv"](https://pkg.go.dev/hash/fnv) 	 | The project adopted this as the default hashing method due to the efficiency and is reasonable for non-cryptographic use 	 |
| Testing  	 | Use `t.Parallel()` where possible            	 | Enabling more tests to be run in parallel will speed up the feedback process when working on the project.                 	 |


Within the project, there are some packages that have yet to follow the recommendations and are being addressed. However, any new code should adhere to the recommendations.

### Default Configuration

To guarantee backward-compatible behavior, all configuration packages should supply a `NewDefault[config name]` functions that create a default version of the config. The package does not need to guarantee that `NewDefault[config name]` returns a usable configuration—only that default values will be set. For example, if the configuration requires that a field, such as `Endpoint` be set, but there is no valid default value, then `NewDefault[config name]` may set that value to `""` with the expectation that the user will set a valid value.

Users should always initialize the config struct with this function and overwrite anything as needed.

### Startup Error Handling

Verify configuration during startup and fail fast if the configuration is invalid.
This will bring the attention of a human to the problem as it is more typical for humans
to notice problems when the process is starting as opposed to problems that may arise
sometime (potentially long time) after process startup. Monitoring systems are likely
to automatically flag processes that exit with failure during startup, making it
easier to notice the problem. The Collector should print a reasonable log message to
explain the problem and exit with a non-zero code. It is acceptable to crash the process
during startup if there is no good way to exit cleanly but do your best to log and
exit cleanly with a process exit code.

### Propagate Errors to the Caller

Do not crash or exit outside the `main()` function, e.g. via `log.Fatal` or `os.Exit`,
even during startup. Instead, return detailed errors to be handled appropriately
by the caller. The code in packages other than `main` may be imported and used by
third-party applications, and they should have full control over error handling
and process termination.

### Do not Crash after Startup

Do not crash or exit the Collector process after the startup sequence is finished.
A running Collector typically contains data that is received but not yet exported further
(e.g. data that is stored in the queues and other processors). Crashing or exiting the Collector
process will result in losing this data since typically the receiver has
already acknowledged the receipt for this data and the senders of the data will
not send that data again.

### Bad Input Handling

Do not crash on bad input in receivers or elsewhere in the pipeline.
[Crash-only software](https://en.wikipedia.org/wiki/Crash-only_software)
is valid in certain cases; however, this is not a correct approach for Collector (except
during startup, see above). The reason is that many senders from which the Collector
receives data have built-in automatic retries of the _same_ data if no
acknowledgment is received from the Collector. If you crash on bad input
chances are high that after the Collector is restarted it will see the same
data in the input and will crash again. This will likely result in an infinite
crashing loop if you have automatic retries in place.

Typically bad input when detected in a receiver should be reported back to the
sender. If it is elsewhere in the pipeline it may be too late to send a response
to the sender (particularly in processors which are not synchronously processing
data). In either case, it is recommended to keep a metric that counts bad input data.

### Error Handling and Retries

Be rigorous in error handling. Don't ignore errors. Think carefully about each
error and decide if it is a fatal problem or a transient problem that may go away
when retried. Fatal errors should be logged or recorded in an internal metric to
provide visibility to users of the Collector. For transient errors come up with a
retrying strategy and implement it. Typically you will
want to implement retries with some sort of exponential back-off strategy. For
connection or sending retries use jitter for back-off intervals to avoid overwhelming
your destination when the network is restored or the destination is recovered.
[Exponential Backoff](https://github.com/cenkalti/backoff) is a good library that
provides all this functionality.

### Logging

Log your component startup and shutdown, including successful outcomes (but don't
overdo it, and keep the number of success messages to a minimum).
This can help to understand the context of failures if they occur elsewhere after
your code is successfully executed.

Use logging carefully for events that can happen frequently to avoid flooding
the logs. Avoid outputting logs per a received or processed data item since this can
amount to a very large number of log entries (Collector is designed to process
many thousands of spans and metrics per second). For such high-frequency events
instead of logging consider adding an internal metric and incrementing it when
the event happens.

Make log messages human readable and also include data that is needed for easier
understanding of what happened and in what context.

### Executing External Processes

The components should avoid executing arbitrary external processes with arbitrary command
line arguments based on user input, including input received from the network or input
read from the configuration file. Failure to follow this rule can result in arbitrary
remote code execution, compelled by malicious actors that can craft the input.

The following limitations are recommended:
- If an external process needs to be executed limit and hard-code the location where
  the executable file may be located, instead of allowing the input to dictate the
  full path to the executable.
- If possible limit the name of the executable file to be pulled from a hard-coded
  list defined at compile time.
- If command line arguments need to be passed to the process do not take the arguments
  from the user input directly. Instead, compose the command line arguments indirectly,
  if necessary, deriving the value from the user input. Limit as much as possible the
  size of the possible space of values for command line arguments.

### Observability

Out of the box, your users should be able to observe the state of your
component. See [observability.md](docs/observability.md) for more details.

When using the regular helpers, you should have some metrics added around key
events automatically. For instance, exporters should have
`otelcol_exporter_sent_spans` tracked without your exporter doing anything.

Custom metrics can be defined as part of the `metadata.yaml` for your component.
The authoritative source of information for this is [the
schema](https://github.com/open-telemetry/opentelemetry-collector/blob/main/cmd/mdatagen/metadata-schema.yaml),
but here are a few examples for reference, adapted from the tail sampling
processor:

```yaml
telemetry:
  metrics:
    # example of a histogram
    processor.tailsampling.samplingdecision.latency:
      description: Latency (in microseconds) of a given sampling policy.
      unit: µs # from https://ucum.org/ucum
      enabled: true
      histogram:
        value_type: int
        # bucket boundaries can be overridden
        bucket_boundaries: [1, 2, 5, 10, 25, 50, 75, 100, 150, 200, 300, 400, 500, 750, 1000, 2000, 3000, 4000, 5000, 10000, 20000, 30000, 50000]

    # example of a counter
    processor.tailsampling.policyevaluation.errors:
      description: Count of sampling policy evaluation errors.
      unit: "{errors}"
      enabled: true
      sum:
        value_type: int
        monotonic: true

    # example of a gauge
    processor.tailsampling.tracesonmemory:
      description: Tracks the number of traces current on memory.
      unit: "{traces}"
      enabled: true
      gauge:
        value_type: int
```

Running `go generate ./...` at the root of your component should generate the
following files:

- `documentation.md`, with the metrics and their descriptions
- `internal/metadata/generated_telemetry.go`, with code that defines the metric
  using the OTel API
- `internal/metadata/generated_telemetry_test.go`, with sanity tests for the
  generated code

On your component's code, you can use the metric by initializing the telemetry
builder and storing it on a component's field:

```go
type tailSamplingSpanProcessor struct {
	ctx context.Context

	telemetry *metadata.TelemetryBuilder
}

func newTracesProcessor(ctx context.Context, settings component.TelemetrySettings, nextConsumer consumer.Traces, cfg Config, opts ...Option) (processor.Traces, error) {
	telemetry, err := metadata.NewTelemetryBuilder(settings)
	if err != nil {
		return nil, err
	}

	tsp := &tailSamplingSpanProcessor{
		ctx:            ctx,
		telemetry:      telemetry,
  }
}
```

To record the measurement, you can then call the metric stored in the telemetry
builder:

```go
tsp.telemetry.ProcessorTailsamplingSamplingdecisionLatency.Record(ctx, ...)
```

### Resource Usage

Limit usage of CPU, RAM, and other resources that the code can use. Do not write code
that consumes resources in an uncontrolled manner. For example, if you have a queue
that can contain unprocessed messages always limit the size of the queue unless you
have other ways to guarantee that the queue will be consumed faster than items are
added to it.

Performance test the code for both normal use-cases under acceptable load and also for
abnormal use-cases when the load exceeds acceptable limits many times over. Ensure that
your code performs predictably under abnormal use. For example, if the code
needs to process received data and cannot keep up with the receiving rate it is
not acceptable to keep allocating more memory for received data until the Collector
runs out of memory. Instead have protections for these situations, e.g. when hitting
resource limits drop the data and record the fact that it was dropped in a metric
that is exposed to users.

### Graceful Shutdown

Collector does not yet support graceful shutdown but we plan to add it. All components
must be ready to shutdown gracefully via `Shutdown()` function that all component
interfaces require. If components contain any temporary data they need to process
and export it out of the Collector before shutdown is completed. The shutdown process
will have a maximum allowed duration so put a limit on how long your shutdown
operation can take.

### Unit Tests

Cover important functionality with unit tests. We require that contributions
do not decrease the overall code coverage of the codebase - this is aligned with our
goal to increase coverage over time. Keep track of execution time for your unit
tests and try to keep them as short as possible.

#### Testing Library Recommendations

To keep testing practices consistent across the project, it is advised to use these libraries under
these circumstances:

- For assertions to validate expectations, use `"github.com/stretchr/testify/assert"`
- For assertions that are required to continue the test, use `"github.com/stretchr/testify/require"`
- For mocking external resources, use `"github.com/stretchr/testify/mock"`
- For validating HTTP traffic interactions, `"net/http/httptest"`

### Integration Testing

Integration testing is encouraged throughout the project, container images can be used in order to facilitate
a local version. In their absence, it is strongly advised to mock the integration.

### Using CGO

Using CGO is prohibited due to the lack of portability and complexity
that comes with managing external libraries with different operating systems and configurations.
However, if the package MUST use CGO, this should be explicitly called out within the readme
with clear instructions on how to install the required libraries.
Furthermore, if your package requires CGO, it MUST be able to compile and operate in a no-op mode
or report a warning back to the collector with a clear error saying CGO is required to work.

### Breaking changes

Whenever possible, we adhere to [semver](https://semver.org/) as our minimum standards. Even before v1, we strive not to break compatibility
without a good reason. Hence, when a change is known to cause a breaking change, it MUST be clearly marked in the
changelog and SHOULD include a line instructing users how to move forward.

We also strive to perform breaking changes in two stages, deprecating it first (`vM.N`) and breaking it in a subsequent
version (`vM.N+1`).

- when we need to remove something, we MUST mark a feature as deprecated in one version and MAY remove it in a
  subsequent one
- when renaming or refactoring types, functions, or attributes, we MUST create the new name and MUST deprecate the old
  one in one version (step 1), and MAY remove it in a subsequent version (step 2). For simple renames, the old name
  SHALL call the new one.
- when a feature is being replaced in favor of an existing one, we MUST mark a feature as deprecated in one version, and
  MAY remove it in a subsequent one.

Deprecation notice SHOULD contain a version starting from which the deprecation takes effect for tracking purposes. For
example, if `GetFoo` function is going to be deprecated in `v0.45.0` version, it gets the following godoc line:

```golang
package test

// Deprecated: [v0.45.0] Use MustDoFoo instead.
func DoFoo() {}
```

#### End-user impacting changes

When deprecating a feature affecting end-users, consider first deprecating the feature in one version, then hiding it
behind a [feature
gate](https://github.com/open-telemetry/opentelemetry-collector/blob/6b5a3d08a96bfb41a5e121b34f592a1d5c6e0435/service/featuregate/)
in a subsequent version, and eventually removing it after yet another version. This is how it would look like, considering
that each of the following steps is done in a separate version:

1. Mark the feature as deprecated, add a short-lived feature gate with the feature enabled by default
1. Change the feature gate to disable the feature by default, deprecating the gate at the same time
1. Remove the feature and the gate

#### Example #1 - Renaming a function

1. Current version `v0.N` has `func GetFoo() Bar`
1. We now decided that `GetBar` is a better name. As such, on `v0.N+1` we add a new `func GetBar() Bar` function,
   changing the existing `func GetFoo() Bar` to be an alias of the new function. Additionally, a log entry with a
   warning is added to the old function, along with an entry to the changelog.
1. On `v0.N+2`, we MAY remove `func GetFoo() Bar`.

#### Example #2 - Changing the return values of a function

1. Current version `v0.N` has `func GetFoo() Foo`
1. We now need to also return an error. We do it by creating a new function that will be equivalent to the existing one
   so that current users can easily migrate to that: `func MustGetFoo() Foo`, which panics on errors. We release this in
   `v0.N+1`, deprecating the existing `func GetFoo() Foo` with it, adding an entry to the changelog and perhaps a log
   entry with a warning.
1. On `v0.N+2`, we change `func GetFoo() Foo` to `func GetFoo() (Foo, error)`.

#### Example #3 - Changing the arguments of a function

1. Current version `v0.N` has `func GetFoo() Foo`
2. We now decide to do something that might be blocking as part of `func GetFoo() Foo`, so, we start accepting a
   context: `func GetFooWithContext(context.Context) Foo`. We release this in `v0.N+1`, deprecating the existing `func
   GetFoo() Foo` with it, adding an entry to the changelog and perhaps a log entry with a warning. The existing `func
   GetFoo() Foo` is changed to call `func GetFooWithContext(context.Background()) Foo`.
3. On `v0.N+2`, we change `func GetFoo() Foo` to `func GetFoo(context.Context) Foo` if desired or remove it entirely if
   needed.

#### Exceptions

For changes to modules that do not have a version of `v1` or higher, we may skip the deprecation process described above
for the following situations. Note that these changes should still be recorded as breaking changes in the changelog.

* **Variadic arguments.** Functions that are not already variadic may have a variadic parameter added as a method of
  supporting optional parameters, particularly through the functional options pattern. If a variadic parameter is
  added to a function with no change in functionality when no variadic arguments are passed, the deprecation process
  may be skipped. Calls to updated functions without the new argument will continue to work before, but users who depend
  on the exact function signature as a type, for example as an argument to another function, will experience a
  breaking change. For this reason, the deprecation process should only be skipped when it is not expected that
  the function is commonly passed as a value.

#### Configuration changes

##### Alpha components

Configuration for alpha components can be changed with minimal notice. Documenting them as part of the changelog is
sufficient. We still recommend giving users one or two minor versions' notice before breaking the configuration, such as
when removing or renaming a configuration option. Providing a migration path in the component's repository is NOT
required for alpha components, although it is still recommended.

- when adding a new configuration option, components MAY mark the new option as required and are not required to provide
  a reasonable default.
- when renaming a configuration option, components MAY treat the old name as an alias to the new one and log a WARN
  level message in case the old option is being used.
- when removing a configuration option, components MAY keep the old option for a few minor releases and log a WARN level
  message instructing users to remove the option.

##### Beta components

One of the requirements for a component to be marked as beta is to have its configuration options stabilized. Therefore,
backward incompatible changes should be rare events for beta components. Users of those components are not expecting to
have their Collector instances failing at startup because of a configuration change. When doing backward incompatible
changes, component owners should add the migration path to a place within the component's repository, linked from the
component's main README. This is to ensure that people using older instructions can understand how to migrate to the
latest version of the component.

When adding a new required option:
- the option MUST come with a sensible default value

When renaming or removing a configuration option:
- the option MUST be deprecated in one version
- a WARN level message should be logged, with a link to a place within the component's repository where the change is
  documented and a migration path is provided
- the option MUST be kept for at least N+1 version and MAY be hidden behind a feature gate in N+2
- the option and the WARN level message MUST NOT be removed earlier than N+2 or 6 months, whichever comes later

Additionally, when removing an option:
- the option MAY be made non-operational already by the same version where it is deprecated

##### Stable components

Stable components MUST be compatible between minor versions unless critical security issues are found. In that case, the
component owner MUST provide a migration path and a reasonable time frame for users to upgrade. The same rules from beta
components apply to stable when it comes to configuration changes.

### Specification Tracking

The [OpenTelemetry Specification](https://github.com/open-telemetry/opentelemetry-specification) can be a rapidly
moving target at times.  While it may seem efficient to get an early start on implementing new features or
functionality under development in the specification, this can also lead to significant churn and a risk that
changes in the specification can result in breaking changes to the implementation.  For this reason, it is the
policy of the Collector SIG to not implement, or accept implementations of, new or changed specification language
prior to inclusion in a stable release of the specification.

## Changelog

### Overview

There are two Changelogs for this repository:

- `CHANGELOG.md` is intended for users of the collector and lists changes that affect the behavior of the collector.
- `CHANGELOG-API.md` is intended for developers who are importing packages from the collector codebase.

### When to add a Changelog Entry

An entry into the changelog is required for the following reasons:

- Changes made to the behaviour of the component
- Changes to the configuration
- Changes to default settings
- New components being added
- Changes to exported elements of a package

It is reasonable to omit an entry to the changelog under these circumstances:

- Updating test to remove flakiness or improve coverage
- Updates to the CI/CD process
- Updates to internal packages

If there is some uncertainty with regards to if a changelog entry is needed, the recommendation is to create
an entry to in the event that the change is important to the project consumers.

### Adding a Changelog Entry

The [CHANGELOG.md](./CHANGELOG.md) and [CHANGELOG-API.md](./CHANGELOG-API.md) files in this repo is autogenerated from `.yaml` files in the `./.chloggen` directory.

Your pull request should add a new `.yaml` file to this directory. The name of your file must be unique since the last release.

During the collector release process, all `./chloggen/*.yaml` files are transcribed into `CHANGELOG.md` and `CHANGELOG-API.md` and then deleted.

**Recommended Steps**
1. Create an entry file using `make chlog-new`. This generates a file based on your current branch (e.g. `./.chloggen/my-branch.yaml`)
2. Fill in all fields in the new file
3. Run `make chlog-validate` to ensure the new file is valid
4. Commit and push the file

Alternatively, copy `./.chloggen/TEMPLATE.yaml`, or just create your file from scratch.

## Release

See [release](docs/release.md) for details.

## Contributing Images
If you are adding any new images, please use [Excalidraw](https://excalidraw.com). It's a free and open-source web application and doesn't require any account to get started. Once you've created the design, while exporting the image, make sure to tick **"Embed scene into exported file"** option. This allows the image to be imported in an editable format for other contributors later.

## Common Issues

Build fails due to dependency issues, e.g.

```sh
go: github.com/golangci/golangci-lint@v1.31.0 requires
	github.com/tommy-muehle/go-mnd@v1.3.1-0.20200224220436-e6f9a994e8fa: invalid pseudo-version: git fetch --unshallow -f origin in /root/go/pkg/mod/cache/vcs/053b1e985f53e43f78db2b3feaeb7e40a2ae482c92734ba3982ca463d5bf19ce: exit status 128:
	fatal: git fetch-pack: expected shallow list
 ```

`go env GOPROXY` should return `https://proxy.golang.org,direct`. If it does not, set it as an environment variable:

`export GOPROXY=https://proxy.golang.org,direct`

### Makefile Guidelines

When adding or modifying the `Makefile`'s in this repository, consider the following design guidelines.

Make targets are organized according to whether they apply to the entire repository, or only to an individual module.
The [Makefile](./Makefile) SHOULD contain "repo-level" targets. (i.e. targets that apply to the entire repo.)
Likewise, `Makefile.Common` SHOULD contain "module-level" targets. (i.e. targets that apply to one module at a time.)
Each module should have a `Makefile` at its root that includes `Makefile.Common`.

#### Module-level targets

Module-level targets SHOULD NOT act on nested modules. For example, running `make lint` at the root of the repo will
_only_ evaluate code that is part of the `go.opentelemetry.io/collector` module. This excludes nested modules such as
`go.opentelemetry.io/collector/component`.

Each module-level target SHOULD have a corresponding repo-level target. For example, `make golint` will run `make lint`
in each module. In this way, the entire repository is covered. The root `Makefile` contains some "for each module" targets
that can wrap a module-level target into a repo-level target.

#### Repo-level targets

Whenever reasonable, targets SHOULD be implemented as module-level targets (and wrapped with a repo-level target).
However, there are many valid justifications for implementing a standalone repo-level target.

1. The target naturally applies to the repo as a whole. (e.g. Building the collector.)
2. Interaction between modules would be problematic.
3. A necessary tool does not provide a mechanism for scoping its application. (e.g. `porto` cannot be limited to a specific module.)
4. The "for each module" pattern would result in incomplete coverage of the codebase. (e.g. A target that scans all files, not just `.go` files.)

#### Default targets

The default module-level target (i.e. running `make` in the context of an individual module), should run a substantial set of module-level
targets for an individual module. Ideally, this would include *all* module-level targets, but exceptions should be made if a particular
target would result in unacceptable latency in the local development loop.

The default repo-level target (i.e. running `make` at the root of the repo) should meaningfully validate the entire repo. This should include
running the default common target for each module as well as additional repo-level targets.

## How to update the OTLP protocol version

When a new OTLP version is published, the following steps are required to update this code base:

1. Edit the top-level Makefile's `OPENTELEMETRY_PROTO_VERSION` variable
2. Run `make genproto` 
3. Inspect modifications to the generated code in `pdata/internal/data/protogen`
4. When new fields are added in the protocol, make corresponding changes in `pdata/internal/cmd/pdatagen/internal`
5. Run `make genpdata` 
6. Inspect modifications to the generated code in `pdata/*`
7. Run `make genproto-cleanup`, to remove temporary files
8. Update the supported OTLP version in [README.md](./README.md).

## Exceptions

While the rules in this and other documents in this repository are what we strive to follow, we acknowledge that it may be unfeasible to apply these rules in some situations. Exceptions to the rules
on this and other documents are acceptable if consensus can be obtained from approvers in the pull request they are proposed.
A reason for requesting the exception MUST be given in the pull request. Until unanimity is obtained, approvers and maintainers are
encouraged to discuss the issue at hand. If a consensus (unanimity) cannot be obtained, the maintainers' group is then tasked with making a
decision using its regular means (voting, TC help, etc.).
