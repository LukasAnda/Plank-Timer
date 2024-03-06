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

decrypt() {
  PASSPHRASE=$1
  INPUT=$2
  OUTPUT=$3
  gpg --quiet --batch --yes --decrypt --passphrase="$PASSPHRASE" --output "$OUTPUT" "$INPUT"
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
  # Decrypt Release key
  decrypt "${ENCRYPT_KEY}" release/app-release.gpg release/app-release.jks
  # Decrypt keystore.properties
  decrypt "${ENCRYPT_KEY}" release/keystore.properties.gpg keystore.properties
  # Decrypt firebase-distribution-account.json
  decrypt "${ENCRYPT_KEY}" release/firebase-distribution-account.gpg release/firebase-distribution-account.json

#  # Decrypt Play Store key
#  decrypt "${ENCRYPT_KEY}" release/play-account.gpg release/play-account.json
  # Decrypt Google Services key (Android)
  decrypt "${ENCRYPT_KEY}" release/google-services.gpg androidApp/google-services.json
  # Decrypt Google Services key (iOS)
  decrypt "${ENCRYPT_KEY}" release/GoogleService-Info.plist.gpg iosApp/iosApp/GoogleService-Info.plist
else
  echo "ENCRYPT_KEY is empty"
fi