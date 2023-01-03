#!/usr/bin/env bash
# Purpose: Roll the version committing the release version
# Usage:
# ./mvn-release.sh

# Echo commands
#set -v  ;

# Command line catch-up release from master
#- git branch release && git checkout release && git pull --quiet origin release ; git rebase master ;
#- git pull origin release ;
#- git push origin release ;
#- git status ;
#- git checkout master ;

# Select current versions and calculate next ones
PROJECT_NAME=$(mvn help:evaluate -Dexpression=project.name | grep -v "^\[" ) ; echo "PROJECT_NAME=\"${PROJECT_NAME?}\"" ;
CURRENT_SNAPSHOT=$(mvn help:evaluate -Dexpression=project.version | grep -v "^\[" ) ; echo "CURRENT_SNAPSHOT=\"${CURRENT_SNAPSHOT?}\"" ;
RELEASE_VERSION=$(echo "${CURRENT_SNAPSHOT?}" | sed -e 's/-SNAPSHOT//') ; echo "RELEASE_VERSION=\"${RELEASE_VERSION?}\"" ;
NEXT_SNAPSHOT="$(echo "${RELEASE_VERSION?}" | sed -e 's/\(.*\)\..*/\1/').$(expr $(echo "${RELEASE_VERSION?}" | sed -e 's/\(.*\)\.\(.*\)/\2/') + 1)-SNAPSHOT"; echo "NEXT_SNAPSHOT=\"${NEXT_SNAPSHOT?}\"" ;

# Validate current snapshot
mvn clean validate ; if [ $? -eq 1 ] ; then echo "Unstable build of ${PROJECT_NAME?} ${CURRENT_SNAPSHOT?}." ; exit 1 ; fi ;
#git add --all ; git commit -m "Prepare for release: ${RELEASE_VERSION?}" ; git push origin master

# Roll to release version and commit
mvn versions:set -DnewVersion="${RELEASE_VERSION?}" -DgenerateBackupPoms="false" ;
mvn --projects parent versions:set -DnewVersion="${RELEASE_VERSION?}" -DgenerateBackupPoms="false" ;
mvn clean validate ; if [ $? -eq 1 ] ; then echo "Unstable build of ${PROJECT_NAME?} ${RELEASE_VERSION?}." ; exit 1 ; fi ;
git add --all ; git commit -m "Release: ${RELEASE_VERSION?}" ; git push origin master

# Roll to next snapshot version and commit
mvn versions:set -DnewVersion="${NEXT_SNAPSHOT?}" -DgenerateBackupPoms="false" ;
mvn --projects parent versions:set -DnewVersion="${NEXT_SNAPSHOT?}" -DgenerateBackupPoms="false" ;
mvn clean validate ; if [ $? -eq 1 ] ; then echo "Unstable build of ${PROJECT_NAME?} ${NEXT_SNAPSHOT?}." ; exit 1 ; fi ;
git add --all ; git commit -m "Roll to next snapshot: ${NEXT_SNAPSHOT?}" 
#git push origin master

# Echo off
#set +v  ;
