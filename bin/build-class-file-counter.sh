#!/bin/bash

set -eu

buildpack_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
vendor_dir="${buildpack_dir}/vendor"

mkdir -p ${vendor_dir}

docker run --rm \
           -v "${buildpack_dir}/class-file-counter":/usr/src \
           -v "${HOME}/.m2":/root/.m2 \
           -w /usr/src \
           oracle/graalvm-ce:19.2.1 \
           ./mvnw clean package -Pgraal

mv ${buildpack_dir}/class-file-counter/target/classes/class-file-counter ${vendor_dir}