#!/usr/bin/env bash
set -euo pipefail

# Build fat jar
echo "Generating jar with runtime dependencies..."
./gradlew shadowJar
echo "Done!"

# Upload fat jar to S3
echo "Copying jar to S3..."
aws s3 cp ./build/libs/cheep-honk.jar s3://cheep-honk/cheep-honk-${GITHUB_SHA:0:8}.jar
echo "Done!"

# Package template.yaml -> packaged-template.yaml
echo "Packaging template.yaml..."
aws cloudformation package \
--template-file ./ops/deployment/template.yaml \
--s3-bucket cheep-honk \
--output-template-file ./ops/deployment/packaged-template.yaml
echo "Done!"
