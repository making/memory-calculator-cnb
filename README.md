# Java Memory Calculator Buildpack

A [Cloud Native Buildpack](https://buildpacks.io) for providing [Memory Calculator](https://github.com/cloudfoundry/java-buildpack-memory-calculator).


## Detection

The detection phase passes if 
* `BP_DISABLE_MEMORY_CALCULATOR` is not set.

## Build

if the build plan contains

* `memory-calculator`
  * Set the output of [Memory Calculator](https://github.com/cloudfoundry/java-buildpack-memory-calculator) to `$JAVA_OPTS`
  * if `$BPL_TOTAL_MEMORY` is specified, configures `--total-memory`. Defaults to `$(cat /sys/fs/cgroup/memory/memory.limit_in_bytes)`. If the unlimited value (= `9223372036854771712`) is set, it will be limited to `1G`.
  * if `$BPL_LOADED_CLASS_COUNT` is specified, configures `--loaded-class-count`. Defaults to the number of class files under `/workspace/` and inside of the jar files.
  * if `$BPL_THREAD_COUNT` is specified, configures `--thread-count`. Defaults to `200`.
  * if `$BPL_HEAD_ROOM` is specified, configures `--head-room`. Defaults to `0`.

## License
Licensed under the Apache License, Version 2.0.