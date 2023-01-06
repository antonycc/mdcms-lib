#!/usr/bin/env bash
# Purpose: Upload binaries to s3
aws s3 cp ./catalogue-bin/target/catalogue-bin-1.*.jar s3://diyaccounting-polycode-install/
aws s3 cp ./gb-web/target/gb-web-1.*.war s3://diyaccounting-polycode-install/

