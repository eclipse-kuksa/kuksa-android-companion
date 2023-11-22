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
