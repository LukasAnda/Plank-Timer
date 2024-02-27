#!/bin/bash

# Copyright 2021 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

encrypt() {
  PASSPHRASE=$1
  INPUT=$2
  OUTPUT=$3
  gpg --batch --yes --passphrase="$PASSPHRASE" --cipher-algo AES256 --symmetric --output "$OUTPUT" "$INPUT"
}

if [[ ! -z "$1" ]]; then
  ENCRYPT_KEY=$1
elif [[ ! -z "$ENCRYPT_KEY" ]]; then
  echo "Using ENCRYPT_KEY from environment variable."
else
  echo "ENCRYPT_KEY is not provided."
  exit 1
fi

if [[ ! -z "$ENCRYPT_KEY" ]]; then
  # Encrypt Release key
  encrypt "${ENCRYPT_KEY}" release/app-release.jks release/app-release.gpg
  # Encrypt keystore.properties
  encrypt "${ENCRYPT_KEY}" keystore.properties release/keystore.properties.gpg
#  # Encrypt Play Store key
#  encrypt "${ENCRYPT_KEY}" release/play-account.json release/play-account.gpg
  # Encrypt Google Services key (Android)
  encrypt "${ENCRYPT_KEY}" androidApp/google-services.json release/google-services.gpg
  # Encrypt Google Services key (iOS)
  encrypt "${ENCRYPT_KEY}" iosApp/iosApp/GoogleService-Info.plist release/GoogleService-Info.plist.gpg
else
  echo "ENCRYPT_KEY is empty"
fi