#!/usr/bin/env bash
# Purpose: Tag and deloy a release version and upload the release evrsion to the install drop-off location
# Usage:
# ./mvn-deploy-release.sh

# Echo commands
#set -v  ;

# Variables
DEST=s3://diyaccounting-polycode-install/

# Select current versions and calculate next ones
PROJECT_NAME=$(mvn help:evaluate -Dexpression=project.name | grep -v "^\[" ) ; echo "PROJECT_NAME=\"${PROJECT_NAME?}\"" ;
CURRENT_SNAPSHOT=$(mvn help:evaluate -Dexpression=project.version | grep -v "^\[" ) ; echo "CURRENT_SNAPSHOT=\"${CURRENT_SNAPSHOT?}\"" ;
RELEASE_VERSION=$(echo "${CURRENT_SNAPSHOT?}" | sed -e 's/-SNAPSHOT//') ; echo "RELEASE_VERSION=\"${RELEASE_VERSION?}\"" ;
NEXT_SNAPSHOT="$(echo "${RELEASE_VERSION?}" | sed -e 's/\(.*\)\..*/\1/').$(expr $(echo "${RELEASE_VERSION?}" | sed -e 's/\(.*\)\.\(.*\)/\2/') + 1)-SNAPSHOT"; echo "NEXT_SNAPSHOT=\"${NEXT_SNAPSHOT?}\"" ;
TAG="${PROJECT_NAME?}-${RELEASE_VERSION?}" ; echo "TAG=\"${TAG?}\"" ;

# Deploy current snapshot abd commit
mvn clean validate ; if [ $? -eq 1 ] ; then echo "Unstable build of ${PROJECT_NAME?} ${CURRENT_SNAPSHOT?}." ; exit 1 ; fi ;
mvn scm:checkin -Dmessage="Prepare for release: ${RELEASE_VERSION?}" ; if [ $? -eq 1 ] ; then echo "Cannot commit ${CURRENT_SNAPSHOT?}." ; exit 1 ; fi ;

# Roll to release version, deploy and commit
mvn versions:set -DnewVersion="${RELEASE_VERSION?}" ;
mvn --projects parent versions:set -DnewVersion="${RELEASE_VERSION?}" ;
mvn versions:commit ;
mvn clean deploy ; if [ $? -eq 1 ] ; then echo "Unstable build of ${PROJECT_NAME?} ${RELEASE_VERSION?}." ; exit 1 ; fi ;
mvn scm:checkin -Dmessage="Release: ${RELEASE_VERSION?}" ; if [ $? -eq 1 ] ; then echo "Cannot commit ${RELEASE_VERSION?}." ; exit 1 ; fi ;
mvn scm:tag -Dtag="${TAG?}" ;

# Roll to next snapshot version, deploy and commit
mvn versions:set -DnewVersion="${NEXT_SNAPSHOT?}" ;
mvn --projects parent versions:set -DnewVersion="${NEXT_SNAPSHOT?}" ;
mvn versions:commit ;
mvn clean validate ; if [ $? -eq 1 ] ; then echo "Unstable build of ${PROJECT_NAME?} ${NEXT_SNAPSHOT?}." ; exit 1 ; fi ;
mvn scm:checkin -Dmessage="Roll to next snaphot: ${NEXT_SNAPSHOT?}" ; if [ $? -eq 1 ] ; then echo "Cannot commit ${NEXT_SNAPSHOT?}." ; exit 1 ; fi ;

# Upload artefacts from local repo to install dropoff
function doS3 {
   SOURCE=${1?} 
   if [ -f "${SOURCE?}.war" ] ; then
      echo "s3 cp ${SOURCE?}.war ${DEST?}"
      aws s3 cp ${SOURCE?}.war ${DEST?}
   else
      if [ -f "${SOURCE?}.jar" ] ; then
         echo "aws s3 cp ${SOURCE?}.jar ${DEST?}"
         aws s3 cp ${SOURCE?}.jar ${DEST?}
      else
         echo "File does not exist: ${SOURCE?}.[jar|war]"
      fi
   fi
}
doS3 ~/.m2/repository/uk/co/diyaccounting/present/catalogue-bin/${RELEASE_VERSION?}/catalogue-bin-${RELEASE_VERSION?}
doS3 ~/.m2/repository/uk/co/diyaccounting/present/messaging-bin/${RELEASE_VERSION?}/messaging-bin-${RELEASE_VERSION?}
doS3 ~/.m2/repository/uk/co/diyaccounting/present/directory-bin/${RELEASE_VERSION?}/directory-bin-${RELEASE_VERSION?}
doS3 ~/.m2/repository/uk/co/diyaccounting/present/gb-web/${RELEASE_VERSION?}/gb-web-${RELEASE_VERSION?}

# Echo off
#set +v  ;
