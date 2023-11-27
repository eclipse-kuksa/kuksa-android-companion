/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

// commit-and-tag-version updater for versioncode;
// see .versionrc

module.exports.readVersion = function (contents) {
  let paddedMajor = contents.substr(2, 2)
  let paddedMinor = contents.substr(4, 2)
  let paddedPatch = contents.substr(6, 2)

  let major = trimPadding(paddedMajor);
  let minor = trimPadding(paddedMinor);
  let patch = trimPadding(paddedPatch);

  return major + "." + minor + "." + patch
};

module.exports.writeVersion = function (contents, version) {
    let versionSplits = version.split(".");
    let unpaddedMajor = versionSplits[0];
    let unpaddedMinor = versionSplits[1];
    let unpaddedPatch = versionSplits[2];

    let decorator = "10";
    let paddedMajor = addPadding(unpaddedMajor);
    let paddedMinor = addPadding(unpaddedMinor);
    let paddedPatch = addPadding(unpaddedPatch);

    return decorator + paddedMajor + paddedMinor + paddedPatch;
};

// removes leading zeros
function trimPadding(paddedNumber) {
  return Number(paddedNumber);
}

// adds padding: 0 => 00; 1 => 01; ... ; 10 => 10
function addPadding(numberToPad) {
  return numberToPad.padStart(2, "0")
}
